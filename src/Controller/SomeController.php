<?php namespace App\Controller;

use App\Service\SessionService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Routing\Annotation\Route;

class SomeController extends AbstractController
{
    #[Route('/some-page', name: 'app_some_page')]
    public function somePage(SessionService $sessionService)
    {
        $user = $sessionService->getUserFromSession();

        if (!$user) {
            $this->addFlash('error', 'Vous devez être connecté pour accéder à cette page.');
            return $this->redirectToRoute('app_login');
        }

        return $this->render('some/page.html.twig', [
            'user' => $user,
        ]);
    }
}