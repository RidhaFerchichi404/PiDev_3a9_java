<?php

namespace App\Controller;

use App\Entity\Exercice;
use App\Form\ExerciceType;
use App\Repository\ExerciceRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\RequestStack;
use App\Entity\User;

#[Route('/exercice')]
final class ExerciceController extends AbstractController
{
    private $requestStack;
    private $entityManager;
    
    public function __construct(RequestStack $requestStack, EntityManagerInterface $entityManager)
    {
        $this->requestStack = $requestStack;
        $this->entityManager = $entityManager;
    }

    #[Route(name: 'app_exercice_index', methods: ['GET'])]
    public function index(ExerciceRepository $exerciceRepository): Response
    {
        return $this->render('exercice/index.html.twig', [
            'exercices' => $exerciceRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_exercice_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour ajouter un exercice.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Créer un nouvel exercice et lui assigner l'utilisateur connecté
        $exercice = new Exercice();
        $exercice->setUser($user);
        
        // Créer le formulaire en cachant le champ utilisateur
        $form = $this->createForm(ExerciceType::class, $exercice, [
            'hide_user' => true,
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur est toujours défini
            if (!$exercice->getUser()) {
                $exercice->setUser($user);
            }
            
            $entityManager->persist($exercice);
            $entityManager->flush();

            return $this->redirectToRoute('app_exercice_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('exercice/new.html.twig', [
            'exercice' => $exercice,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_exercice_show', methods: ['GET'])]
    public function show(Exercice $exercice): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        $user = $this->getUserFromSession();
        
        // Si l'utilisateur n'est pas connecté ou n'est pas propriétaire/admin, rediriger vers la page publique
        if (!$user || ($exercice->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN')) {
            return $this->render('exercice/show_public.html.twig', [
                'exercice' => $exercice,
            ]);
        }
        
        // Afficher la vue complète avec options d'édition pour le propriétaire ou l'admin
        return $this->render('exercice/show.html.twig', [
            'exercice' => $exercice,
            'is_owner' => ($user && $exercice->getUser()->getId() === $user->getId()),
            'is_admin' => ($user && $user->getRole() === 'ROLE_ADMIN'),
        ]);
    }

    #[Route('/exercice/{id}', name: 'app_exercice_showf', methods: ['GET'])]
    public function showf(Exercice $exercice): Response
    {
        // Cette méthode est pour l'affichage public
        return $this->render('exercice/show_public.html.twig', [
            'exercice' => $exercice,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_exercice_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Exercice $exercice, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour modifier un exercice.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Vérifier que l'utilisateur est propriétaire de l'exercice ou est admin
        if ($exercice->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à modifier cet exercice.');
            return $this->redirectToRoute('app_exercice_index');
        }
        
        // Sauvegarder l'utilisateur original
        $originalUser = $exercice->getUser();
        
        // Créer le formulaire en cachant le champ utilisateur
        $form = $this->createForm(ExerciceType::class, $exercice, [
            'hide_user' => true,
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur n'a pas été modifié
            $exercice->setUser($originalUser);
            
            $entityManager->flush();

            // Rediriger vers les exercices de l'équipement associé
            return $this->redirectToRoute('app_equipement_exercices', [
                'id' => $exercice->getEquipement()->getId(),
            ], Response::HTTP_SEE_OTHER);
        }

        return $this->render('exercice/edit.html.twig', [
            'exercice' => $exercice,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_exercice_delete', methods: ['POST'])]
    public function delete(Request $request, Exercice $exercice, EntityManagerInterface $entityManager): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour supprimer un exercice.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Vérifier que l'utilisateur est propriétaire de l'exercice ou est admin
        if ($exercice->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à supprimer cet exercice.');
            return $this->redirectToRoute('app_exercice_index');
        }
        
        if ($this->isCsrfTokenValid('delete'.$exercice->getId(), $request->getPayload()->getString('_token'))) {
            $entityManager->remove($exercice);
            $entityManager->flush();
            $this->addFlash('success', 'Exercice supprimé avec succès.');
        }

        return $this->redirectToRoute('app_exercice_index', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/new/equipement/{id}', name: 'app_exercice_new_for_equipement', methods: ['GET', 'POST'])]
    public function newForEquipement(Request $request, EntityManagerInterface $entityManager, $id): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour ajouter un exercice.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'équipement
        $equipement = $entityManager->getRepository('App\Entity\Equipement')->find($id);
        
        if (!$equipement) {
            throw $this->createNotFoundException('L\'équipement n\'existe pas');
        }
        
        // Vérifier si l'utilisateur est le propriétaire de l'équipement ou est admin
        if ($equipement->getUser()->getId() !== $user->getId() && $user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Vous n\'êtes pas autorisé à ajouter des exercices à cet équipement.');
            return $this->redirectToRoute('app_equipement_index');
        }
        
        // Créer un nouvel exercice
        $exercice = new Exercice();
        
        // Préremplir l'équipement
        $exercice->setEquipement($equipement);
        
        // Associer l'utilisateur connecté à l'exercice
        $exercice->setUser($user);
        
        // Créer le formulaire avec options pour masquer les champs déjà définis
        $form = $this->createForm(ExerciceType::class, $exercice, [
            'hide_equipement' => true,
            'hide_user' => true,
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // S'assurer que l'utilisateur et l'équipement sont toujours définis
            if (!$exercice->getUser()) {
                $exercice->setUser($user);
            }
            if (!$exercice->getEquipement()) {
                $exercice->setEquipement($equipement);
            }
            
            $entityManager->persist($exercice);
            $entityManager->flush();

            // Rediriger vers la liste des exercices de cet équipement
            return $this->redirectToRoute('app_equipement_exercices', ['id' => $id], Response::HTTP_SEE_OTHER);
        }

        return $this->render('exercice/new.html.twig', [
            'exercice' => $exercice,
            'form' => $form,
            'equipement_id' => $id,
            'equipement_nom' => $equipement->getNom(),
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
