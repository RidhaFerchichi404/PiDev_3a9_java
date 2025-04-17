<?php

namespace App\Controller;

use App\Entity\SalleDeSport;
use App\Entity\Historique;
use App\Form\SalleDeSportType;
use App\Repository\SalleDeSportRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Entity\User;
use Symfony\Component\HttpFoundation\RequestStack;

#[Route('/salle/de/sport')]
final class SalleDeSportController extends AbstractController
{
    private $requestStack;
    private $entityManager;

    public function __construct(RequestStack $requestStack, EntityManagerInterface $entityManager)
    {
        $this->requestStack = $requestStack;
        $this->entityManager = $entityManager;
    }

    #[Route(name: 'app_salle_de_sport_index', methods: ['GET'])]
    public function index(SalleDeSportRepository $salleDeSportRepository, EntityManagerInterface $entityManager): Response
    {
        $salleDeSports = $salleDeSportRepository->findAll();

        // Récupérer les équipements et les classer
        $to_do = [];
        $in_progress = [];
        $done = [];
        $data = [];
        $data_values = [];
        $equipements_data = []; // Ajouter cette variable

        $aujourdhui = new \DateTime();

        foreach ($salleDeSports as $salle) {
            $equipements = $salle->getEquipements();
            $data[] = $salle->getNom(); // Nom de la salle
            $data_values[] = count($equipements); // Nombre d'équipements

            foreach ($equipements as $equipement) {
                $prochaineVerification = $equipement->getProchaineVerification();
                $derniereVerification = $equipement->getDerniereVerification();

                // Ajouter les données des équipements pour le calendrier
                $equipements_data[] = [
                    'nom' => $equipement->getNom(),
                    'salle' => $salle->getNom(),
                    'derniere_verification' => $derniereVerification,
                    'prochaine_verification' => $prochaineVerification,
                ];

                if ($prochaineVerification > $aujourdhui) {
                    $to_do[] = [
                        'nom' => $equipement->getNom(),
                        'salle' => $salle->getNom(),
                        'prochaine_verification' => $prochaineVerification,
                    ];
                } elseif ($prochaineVerification == $aujourdhui) {
                    $in_progress[] = [
                        'nom' => $equipement->getNom(),
                        'salle' => $salle->getNom(),
                        'prochaine_verification' => $prochaineVerification,
                    ];
                } else {
                    $done[] = [
                        'nom' => $equipement->getNom(),
                        'salle' => $salle->getNom(),
                        'prochaine_verification' => $prochaineVerification,
                    ];
                }
            }
        }

        return $this->render('salle_de_sport/index.html.twig', [
            'salle_de_sports' => $salleDeSports,
            'to_do' => $to_do,
            'in_progress' => $in_progress,
            'done' => $done,
            'data' => $data, // Labels des salles
            'data_values' => $data_values, // Valeurs des équipements
            'equipements_data' => $equipements_data, // Données des équipements pour le calendrier
        ]);
    }

    #[Route('/fronts',name: 'app_salle_de_sport_index1', methods: ['GET'])]
    public function index1(SalleDeSportRepository $salleDeSportRepository): Response
    {
        return $this->render('salle_de_sport/front.html.twig', [
            'salle_de_sports' => $salleDeSportRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_salle_de_sport_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        if (!$this->requestStack->getSession()->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour ajouter une salle de sport.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        $salleDeSport = new SalleDeSport();
        // Associer l'utilisateur à la salle
        $salleDeSport->setUser($user);
        
        $form = $this->createForm(SalleDeSportType::class, $salleDeSport, [
            'hide_user' => true, // Masquer le champ utilisateur car il est défini automatiquement
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur est bien assigné
            if (!$salleDeSport->getUser()) {
                $salleDeSport->setUser($user);
            }
            
            $entityManager->persist($salleDeSport);
            $entityManager->flush();

            // Ajouter une entrée dans l'historique
            $historique = new Historique();
            $historique->setAction('Ajout');
            $historique->setSalle($salleDeSport->getNom());
            $historique->setDate(new \DateTime());
            $entityManager->persist($historique);
            $entityManager->flush();

            return $this->redirectToRoute('app_salle_de_sport_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('salle_de_sport/new.html.twig', [
            'salle_de_sport' => $salleDeSport,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_salle_de_sport_show', methods: ['GET'])]
    public function show(SalleDeSport $salleDeSport): Response
    {
        // Vérifier si l'utilisateur est connecté
        $user = $this->getUserFromSession();
        
        // Si l'utilisateur n'est pas connecté ou n'est pas propriétaire/admin, afficher la vue publique
        if (!$user || ($salleDeSport->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN')) {
            return $this->render('salle_de_sport/show_public.html.twig', [
                'salle_de_sport' => $salleDeSport,
            ]);
        }
        
        // Afficher la vue complète avec options d'édition pour le propriétaire ou l'admin
        return $this->render('salle_de_sport/show.html.twig', [
            'salle_de_sport' => $salleDeSport,
            'is_owner' => ($user && $salleDeSport->getUser()->getId() === $user->getId()),
            'is_admin' => ($user && $user->getRole() === 'ROLE_ADMIN'),
        ]);
    }

    #[Route('/{id}/edit', name: 'app_salle_de_sport_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, SalleDeSport $salleDeSport, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        if (!$this->requestStack->getSession()->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour modifier une salle de sport.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Vérifier si l'utilisateur est le propriétaire de la salle ou un admin
        if ($salleDeSport->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier cette salle de sport.');
            return $this->redirectToRoute('app_salle_de_sport_index1');
        }
        
        $form = $this->createForm(SalleDeSportType::class, $salleDeSport, [
            'hide_user' => true, // Ne pas permettre de changer le propriétaire
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            
            $this->addFlash('success', 'Salle de sport modifiée avec succès.');
            return $this->redirectToRoute('app_salle_de_sport_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('salle_de_sport/edit.html.twig', [
            'salle_de_sport' => $salleDeSport,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_salle_de_sport_delete', methods: ['POST'])]
    public function delete(Request $request, SalleDeSport $salleDeSport, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        if (!$this->requestStack->getSession()->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour supprimer une salle de sport.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Vérifier si l'utilisateur est le propriétaire de la salle ou un admin
        if ($salleDeSport->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à supprimer cette salle de sport.');
            return $this->redirectToRoute('app_salle_de_sport_index1');
        }
        
        if ($this->isCsrfTokenValid('delete'.$salleDeSport->getId(), $request->getPayload()->getString('_token'))) {
            $entityManager->remove($salleDeSport);
            $entityManager->flush();
            $this->addFlash('success', 'Salle de sport supprimée avec succès.');
        }

        return $this->redirectToRoute('app_salle_de_sport_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}/equipements', name: 'app_salle_de_sport_equipements', methods: ['GET'])]
    public function equipements(SalleDeSport $salleDeSport): Response
    {
        return $this->render('equipement/index.html.twig', [
            'equipements' => $salleDeSport->getEquipements(),
            'salle_de_sport' => $salleDeSport,
        ]);
    }

    #[Route('/{id}/equipementsf', name: 'app_salle_de_sport_equipementsf', methods: ['GET'])]
    public function equipementsf(SalleDeSport $salleDeSport): Response
    {
        $equipements = $salleDeSport->getEquipements();
    
        return $this->render('equipement/front.html.twig', [
            'salle_de_sport' => $salleDeSport,
            'equipements' => $equipements,
        ]);
    }

    #[Route('/statistics', name: 'app_salle_de_sport_statistics', methods: ['GET'])]
    public function stat(SalleDeSportRepository $salleDeSportRepository): Response
    {
        // Fetch the salle de sport data
        $salleDeSports = $salleDeSportRepository->findAll();

        // Create data arrays for the chart (labels and values)
        $data = [];
        $data_values = [];

        foreach ($salleDeSports as $salleDeSport) {
            // Vérifier que la salle a un nom avant de l'ajouter
            if ($salleDeSport->getNom()) {
                $data[] = $salleDeSport->getNom();  // The name of the gym
                $data_values[] = $salleDeSport->getEquipements()->count();  // The number of equipment in the gym
            }
        }

        // S'assurer qu'il y a des données à afficher
        if (empty($data)) {
            $data = ['Aucune salle'];
            $data_values = [0];
        }

        return $this->render('salle_de_sport/statistics.html.twig', [
            'salle_de_sports' => $salleDeSports,
            'data' => $data,             // Pass the labels
            'data_values' => $data_values // Pass the values
        ]);
    }

    #[Route('/{id}/pdf', name: 'app_salle_de_sport_pdf', methods: ['GET'])]
    public function generatePdf(SalleDeSport $salleDeSport): Response
    {
        // Inclure TCPDF
        require_once $this->getParameter('kernel.project_dir') . '/vendor/tcpdf/tcpdf.php';

        // Créer une instance de TCPDF
        $pdf = new \TCPDF();

        // Configurer le document PDF
        $pdf->SetCreator('Symfony');
        $pdf->SetAuthor('Gym Dashboard');
        $pdf->SetTitle($salleDeSport->getNom() . ' - Details');
        $pdf->SetSubject('Gym Details');
        $pdf->SetMargins(10, 10, 10);
        $pdf->SetAutoPageBreak(true, 10);

        // Ajouter une page
        $pdf->AddPage();

        // Contenu HTML pour le PDF
        $html = $this->renderView('salle_de_sport/pdf.html.twig', [
            'salle_de_sport' => $salleDeSport,
            'equipements' => $salleDeSport->getEquipements(),
        ]);

        // Charger le contenu HTML dans TCPDF
        $pdf->writeHTML($html, true, false, true, false, '');

        // Générer le PDF
        $pdfContent = $pdf->Output($salleDeSport->getNom() . '_details.pdf', 'S');

        // Retourner le PDF en réponse
        return new Response($pdfContent, 200, [
            'Content-Type' => 'application/pdf',
            'Content-Disposition' => 'inline; filename="' . $salleDeSport->getNom() . '_details.pdf"',
        ]);
    }
    #[Route('/ask-chatgpt', name: 'ask_chatgpt')]
    public function askChatGPT(Request $request): Response
    {
        $userMessage = $request->query->get('message', 'What is the weather today?'); // Exemple de message par défaut

        $response = $this->chatGPTService->askChatGPT($userMessage);

        return $this->render('chatgpt/response.html.twig', [
            'response' => $response,
        ]);
    }

    // Méthode pour récupérer l'utilisateur connecté depuis la session
    private function getUserFromSession(): ?User
    {
        // Utiliser d'abord le système de sécurité de Symfony
        if ($this->getUser()) {
            return $this->getUser();
        }
        
        // Fallback à l'ancienne méthode basée sur la session pour la compatibilité
        if (!$this->requestStack->getSession()->has('user_id')) {
            return null;
        }
        
        $userId = $this->requestStack->getSession()->get('user_id');
        return $this->entityManager->getRepository(User::class)->find($userId);
    }
}
