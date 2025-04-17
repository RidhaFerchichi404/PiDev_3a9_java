<?php

namespace App\Controller;

use App\Entity\Equipement;
use App\Form\EquipementType;
use App\Repository\EquipementRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\RequestStack;
use App\Entity\User;

#[Route('/equipement')]
final class EquipementController extends AbstractController
{
    private $requestStack;
    private $entityManager;
    
    public function __construct(RequestStack $requestStack, EntityManagerInterface $entityManager)
    {
        $this->requestStack = $requestStack;
        $this->entityManager = $entityManager;
    }

    #[Route(name: 'app_equipement_index', methods: ['GET'])]
    public function index(EquipementRepository $equipementRepository): Response
    {
        return $this->render('equipement/index.html.twig', [
            'equipements' => $equipementRepository->findAll(),
        ]);
    }
    #[Route('/front', name: 'app_equipement', methods: ['GET', 'POST'])]
    public function index1(EquipementRepository $equipementRepository): Response
    {
        return $this->render('equipement/front.html.twig', [
            'equipements' => $equipementRepository->findAll(),
        ]);
    }


    #[Route('/new', name: 'app_equipement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour ajouter un équipement.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Créer un nouvel équipement et définir l'utilisateur
        $equipement = new Equipement();
        $equipement->setIdUser($user->getId());
        $equipement->setEntityManager($entityManager);
        
        // Créer le formulaire en masquant le champ utilisateur
        $form = $this->createForm(EquipementType::class, $equipement, [
            'hide_user' => true
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur est toujours défini
            if (!$equipement->getIdUser()) {
                $equipement->setIdUser($user->getId());
            }
            
            $entityManager->persist($equipement);
            $entityManager->flush();

            return $this->redirectToRoute('app_equipement_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('equipement/new.html.twig', [
            'equipement' => $equipement,
            'form' => $form,
        ]);
    }

    #[Route('/new/salle/{id}', name: 'app_equipement_new_for_salle', methods: ['GET', 'POST'])]
    public function newForSalle(Request $request, EntityManagerInterface $entityManager, $id): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour ajouter un équipement.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Créer un nouvel équipement
        $equipement = new Equipement();
        $equipement->setEntityManager($entityManager);
        
        // Récupérer la salle de sport
        $salle = $entityManager->getRepository('App\Entity\SalleDeSport')->find($id);
        
        if (!$salle) {
            throw $this->createNotFoundException('La salle n\'existe pas');
        }
        
        // Vérifier si l'utilisateur est propriétaire de la salle ou est admin
        if ($salle->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à ajouter des équipements à cette salle.');
            return $this->redirectToRoute('app_salle_de_sport_index1');
        }
        
        // Préremplir la salle
        $equipement->setSalle($salle);
        
        // Associer l'utilisateur connecté à l'équipement
        $equipement->setIdUser($user->getId());
        
        // Créer le formulaire avec les options pour masquer les champs déjà définis
        $form = $this->createForm(EquipementType::class, $equipement, [
            'hide_salle' => true,
            'hide_user' => true,
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur et la salle sont toujours définis
            if (!$equipement->getIdUser()) {
                $equipement->setIdUser($user->getId());
            }
            if (!$equipement->getSalle()) {
                $equipement->setSalle($salle);
            }
            
            $entityManager->persist($equipement);
            $entityManager->flush();

            // Rediriger vers la liste des équipements de cette salle
            return $this->redirectToRoute('app_salle_de_sport_equipements', ['id' => $id], Response::HTTP_SEE_OTHER);
        }

        return $this->render('equipement/new.html.twig', [
            'equipement' => $equipement,
            'form' => $form,
            'salle_id' => $id,
            'salle_nom' => $salle->getNom(),
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_show', methods: ['GET'])]
    public function show(Equipement $equipement): Response
    {
        return $this->render('equipement/show.html.twig', [
            'equipement' => $equipement,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_equipement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Equipement $equipement, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour modifier un équipement.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Injecter l'EntityManager pour que getUser() fonctionne
        $equipement->setEntityManager($entityManager);
        
        // Vérifier si l'utilisateur est propriétaire de l'équipement ou admin
        if ($equipement->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier cet équipement.');
            return $this->redirectToRoute('app_equipement_index');
        }
        
        // Sauvegarder le propriétaire original
        $originalUserId = $equipement->getIdUser();
        
        // Créer le formulaire en masquant le champ utilisateur
        $form = $this->createForm(EquipementType::class, $equipement, [
            'hide_user' => true
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur propriétaire n'a pas été modifié
            $equipement->setIdUser($originalUserId);
            
            $entityManager->flush();

            // Rediriger vers les équipements de la salle correspondante
            return $this->redirectToRoute('app_salle_de_sport_equipements', [
                'id' => $equipement->getSalle()->getId(),
            ], Response::HTTP_SEE_OTHER);
        }

        return $this->render('equipement/edit.html.twig', [
            'equipement' => $equipement,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_equipement_delete', methods: ['POST'])]
    public function delete(Request $request, Equipement $equipement, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour supprimer un équipement.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Injecter l'EntityManager pour que getUser() fonctionne
        $equipement->setEntityManager($entityManager);
        
        // Vérifier si l'utilisateur est propriétaire de l'équipement ou admin
        if ($equipement->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à supprimer cet équipement.');
            return $this->redirectToRoute('app_equipement_index');
        }
        
        if ($this->isCsrfTokenValid('delete'.$equipement->getId(), $request->getPayload()->getString('_token'))) {
            $entityManager->remove($equipement);
            $entityManager->flush();
            $this->addFlash('success', 'Équipement supprimé avec succès.');
        }

        return $this->redirectToRoute('app_equipement_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/{id}/exercices', name: 'app_equipement_exercices', methods: ['GET'])]
    public function exercices(Equipement $equipement): Response
    {
        return $this->render('exercice/index.html.twig', [
            'exercices' => $equipement->getExercices(),
            'equipement' => $equipement,
        ]);
    }

    #[Route('/{id}/exercicesf', name: 'app_equipement_exercicesf', methods: ['GET'])]
    public function exercices1(Equipement $equipement): Response
    {
        $exercices = $equipement->getExercices();
    
        return $this->render('exercice/front.html.twig', [
            'equipement' => $equipement,
            'exercices' => $exercices,
        ]);
    }

    // Méthode pour récupérer l'utilisateur connecté depuis la session
    private function getUserFromSession(): ?User
    {
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            return null;
        }
        
        $userId = $session->get('user_id');
        return $this->entityManager->getRepository(User::class)->find($userId);
    }
}
