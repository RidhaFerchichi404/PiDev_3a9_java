<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Doctrine\ORM\EntityManagerInterface;
use App\Entity\Historique;
use App\Entity\User;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use App\Repository\HistoriqueRepository;

#[Route('/historique')]
final class HistoriqueController extends AbstractController
{
    #[Route(name: 'app_historique', methods: ['GET'])]
    public function historique(HistoriqueRepository $historiqueRepository, EntityManagerInterface $entityManager, SessionInterface $session): Response
    {
        // Verify user is logged in
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour accéder à l\'historique.');
            return $this->redirectToRoute('app_login');
        }
        
        // Get user from session
        $userId = $session->get('user_id');
        $user = $entityManager->getRepository(User::class)->find($userId);
        
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Check if user has admin role
        if ($user->getRole() !== 'ROLE_ADMIN') {
            $this->addFlash('error', 'Accès restreint. Seuls les administrateurs peuvent consulter l\'historique.');
            return $this->redirectToRoute('app_salle_de_sport_index1'); 
        }
        
        return $this->render('historique/index.html.twig', [
            'historiques' => $historiqueRepository->findAll(),
        ]);
    }

    // Method to get the user from session
    private function getUserFromSession(EntityManagerInterface $entityManager, SessionInterface $session): ?User
    {
        if (!$session->has('user_id')) {
            return null;
        }
        
        $userId = $session->get('user_id');
        return $entityManager->getRepository(User::class)->find($userId);
    }
}
