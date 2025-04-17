<?php namespace App\Twig;

use Symfony\Component\HttpFoundation\RequestStack;
use Twig\Extension\AbstractExtension;
use Twig\TwigFunction;

class SessionExtension extends AbstractExtension
{
    private $requestStack;

    public function __construct(RequestStack $requestStack)
    {
        $this->requestStack = $requestStack;
    }

    public function getFunctions(): array
    {
        return [
            new TwigFunction('get_session', [$this, 'getSession']),
        ];
    }

    public function getSession(string $key)
    {
        $session = $this->requestStack->getSession();
        return $session->get($key);
    }
}