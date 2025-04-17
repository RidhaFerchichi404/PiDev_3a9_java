<?php

namespace App\Security;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Exception\AuthenticationException;
use Symfony\Component\Security\Core\Exception\CustomUserMessageAuthenticationException;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\UserProviderInterface;
use Symfony\Component\Security\Http\Authenticator\AbstractAuthenticator;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\CsrfTokenBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\RememberMeBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\Badge\UserBadge;
use Symfony\Component\Security\Http\Authenticator\Passport\Credentials\PasswordCredentials;
use Symfony\Component\Security\Http\Authenticator\Passport\Passport;
use Symfony\Component\Security\Http\EntryPoint\AuthenticationEntryPointInterface;
use Symfony\Component\Security\Http\Util\TargetPathTrait;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

class LoginFormAuthenticator extends AbstractAuthenticator implements AuthenticationEntryPointInterface
{
    use TargetPathTrait;

    public const LOGIN_ROUTE = 'app_login';

    private EntityManagerInterface $entityManager;
    private UrlGeneratorInterface $urlGenerator;
    private UserPasswordHasherInterface $passwordHasher;

    public function __construct(
        EntityManagerInterface $entityManager,
        UrlGeneratorInterface $urlGenerator,
        UserPasswordHasherInterface $passwordHasher
    ) {
        $this->entityManager = $entityManager;
        $this->urlGenerator = $urlGenerator;
        $this->passwordHasher = $passwordHasher;
    }

    public function supports(Request $request): bool
    {
        return self::LOGIN_ROUTE === $request->attributes->get('_route')
            && $request->isMethod('POST');
    }

    public function authenticate(Request $request): Passport
    {
        $email = $request->request->get('email', '');
        $password = $request->request->get('password', '');
        $csrfToken = $request->request->get('_csrf_token', '');

        // Recherche de l'utilisateur
        $user = $this->entityManager->getRepository(User::class)->findOneBy(['email' => $email]);
        
        if (!$user) {
            throw new CustomUserMessageAuthenticationException('Email non trouvé.');
        }
        
        if (!$user->getIsactive()) {
            throw new CustomUserMessageAuthenticationException('Votre compte est désactivé.');
        }
        
        // Utilisation du système de sécurité de Symfony pour la vérification
        return new Passport(
            new UserBadge($email),
            new PasswordCredentials($password),
            [
                new CsrfTokenBadge('authenticate', $csrfToken),
                new RememberMeBadge(),
            ]
        );
    }

    public function onAuthenticationSuccess(Request $request, TokenInterface $token, string $firewallName): ?Response
    {
        $user = $token->getUser();
        $session = $request->getSession();
        
        // Store user information in session for backward compatibility with the existing code
        $session->set('user_id', $user->getId());
        $session->set('user_role', $user->getRole());
        $session->set('user_name', $user->getFirstname() . ' ' . $user->getLastname());
        
        if ($targetPath = $this->getTargetPath($request->getSession(), $firewallName)) {
            return new RedirectResponse($targetPath);
        }

        // Redirect based on user role
        if ($user->getRole() === 'ROLE_ADMIN') {
            return new RedirectResponse($this->urlGenerator->generate('app_salle_de_sport_index'));
        } else {
            return new RedirectResponse($this->urlGenerator->generate('app_salle_de_sport_index1'));
        }
    }

    public function onAuthenticationFailure(Request $request, AuthenticationException $exception): ?Response
    {
        if ($request->hasSession()) {
            $request->getSession()->set(Security::AUTHENTICATION_ERROR, $exception);
            $request->getSession()->set(Security::LAST_USERNAME, $request->request->get('email'));
        }

        return new RedirectResponse($this->urlGenerator->generate('app_login'));
    }

    public function start(Request $request, AuthenticationException $authException = null): Response
    {
        return new RedirectResponse(
            $this->urlGenerator->generate('app_login')
        );
    }

    /**
     * Method to check if the plain password matches the hashed one
     */
    private function isPasswordValid(UserInterface $user, string $plainPassword): bool
    {
        if ($user instanceof User) {
            // Utiliser correctement le hasher de mot de passe
            return $this->passwordHasher->isPasswordValid($user, $plainPassword);
        }
        
        return false;
    }
} 