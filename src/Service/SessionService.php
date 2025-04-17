<?php 
namespace App\Service;

use Symfony\Component\HttpFoundation\RequestStack;

class SessionService
{
    private $requestStack;

    public function __construct(RequestStack $requestStack)
    {
        $this->requestStack = $requestStack;
    }

    public function getUserFromSession(): ?array
    {
        $session = $this->requestStack->getSession();
        if ($session->has('user_id')) {
            return [
                'id' => $session->get('user_id'),
                'name' => $session->get('user_name'),
                'role' => $session->get('user_role'),
            ];
        }

        return null;
    }
}