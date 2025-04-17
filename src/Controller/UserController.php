<?php

namespace App\Controller;

use App\Entity\User;
use App\Entity\SalleDeSport;
use App\Entity\Equipement;
use App\Form\UserType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\RequestStack;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\PasswordHasher\Hasher\UserPasswordHasherInterface;

final class UserController extends AbstractController
{
    private $requestStack;
    private $entityManager;
    private $passwordHasher;
    
    public function __construct(RequestStack $requestStack, EntityManagerInterface $entityManager, UserPasswordHasherInterface $passwordHasher)
    {
        $this->requestStack = $requestStack;
        $this->entityManager = $entityManager;
        $this->passwordHasher = $passwordHasher;
    }

    #[Route('/', name: 'app_homepage')]
    public function home(): Response
    {
        // Si l'utilisateur est déjà connecté, le rediriger
        if ($this->getUserFromSession()) {
            if ($this->checkUserRole('ROLE_ADMIN')) {
                return $this->redirectToRoute('app_user_index');
            } else {
                return $this->redirectToRoute('app_front_homepage');
            }
        }
        
        // Sinon, afficher la page d'accueil
        return $this->render('front/homepage.html.twig');
    }
    
    #[Route('/login', name: 'app_login')]
    public function login(Request $request): Response
    {
        // Vérifier si l'utilisateur est déjà connecté
        $session = $this->requestStack->getSession();
        if ($session->has('user_id')) {
            // Si c'est un admin, rediriger vers le dashboard admin
            if ($session->get('user_role') === 'ROLE_ADMIN') {
                return $this->redirectToRoute('app_salle_de_sport_index');
            }
            
            // Si c'est un utilisateur normal, rediriger vers l'accueil front
            return $this->redirectToRoute('app_salle_de_sport_index1');
        }
        
        // Récupérer les paramètres
        $error = $request->query->get('error');
        $email = $request->query->get('email', '');
        
        return $this->render('front/login.html.twig', [
            'error' => $error,
            'email' => $email,
        ]);
    }
    
    #[Route('/login_check', name: 'app_login_check', methods: ['POST'])]
    public function loginCheck(Request $request): Response
    {
        // Récupérer les données du formulaire
        $email = $request->request->get('email');
        $password = $request->request->get('password');
        $rememberMe = $request->request->get('_remember_me', false);
        
        // Validation basique
        if (empty($email) || empty($password)) {
            return $this->redirectToRoute('app_login', [
                'error' => 'L\'email et le mot de passe sont requis',
                'email' => $email
            ]);
        }
        
        // Recherche de l'utilisateur par email
        $user = $this->entityManager->getRepository(User::class)->findOneBy(['email' => $email]);
        
        // Vérifier si l'utilisateur existe
        if (!$user) {
            return $this->redirectToRoute('app_login', [
                'error' => 'Aucun compte trouvé avec cet email',
                'email' => $email
            ]);
        }
        
        // Vérifier si le compte est actif
        if (!$user->getIsactive()) {
            return $this->redirectToRoute('app_login', [
                'error' => 'Votre compte est désactivé',
                'email' => $email
            ]);
        }
        
        // Vérifier le mot de passe avec le hasher
        if (!$this->passwordHasher->isPasswordValid($user, $password)) {
            return $this->redirectToRoute('app_login', [
                'error' => 'Le mot de passe est invalide',
                'email' => $email
            ]);
        }
        
        // Authentification réussie, création de la session
        $session = $this->requestStack->getSession();
        $session->set('user_id', $user->getId());
        $session->set('user_role', $user->getRole());
        $session->set('user_name', $user->getFirstname() . ' ' . $user->getLastname());
        
        // Redirection basée sur le rôle
        if ($user->getRole() === 'ROLE_ADMIN') {
            return $this->redirectToRoute('app_salle_de_sport_index');
        } else {
            return $this->redirectToRoute('app_salle_de_sport_index1');
        }
    }
    
    #[Route('/logout', name: 'app_user_logout')]
    public function logout(): Response
    {
        $session = $this->requestStack->getSession();
        $session->remove('user_id');
        $session->remove('user_role');
        $session->remove('user_name');
        
        $this->addFlash('success', 'Vous avez été déconnecté.');
        
        return $this->redirectToRoute('app_homepage');
    }
    
    #[Route('/register', name: 'app_register')]
    public function register(Request $request): Response
    {
        // Si l'utilisateur est déjà connecté, rediriger
        if ($this->getUserFromSession()) {
            return $this->redirectToRoute('app_homepage');
        }
        
        $error = null;
        $success = null;
        
        if ($request->isMethod('POST')) {
            $firstname = $request->request->get('firstname');
            $lastname = $request->request->get('lastname');
            $email = $request->request->get('email');
            $password = $request->request->get('password');
            $passwordConfirm = $request->request->get('password_confirm');
            $phoneNumber = $request->request->get('phone_number');
            
            // Validation basique
            if (empty($firstname) || empty($lastname) || empty($email) || empty($password)) {
                $error = 'Les champs prénom, nom, email et mot de passe sont obligatoires.';
            } elseif ($password !== $passwordConfirm) {
                $error = 'Les mots de passe ne correspondent pas.';
            } elseif (strlen($password) < 6) {
                $error = 'Le mot de passe doit contenir au moins 6 caractères.';
            } else {
                // Vérifier si l'email existe déjà
                $userRepository = $this->entityManager->getRepository(User::class);
                $existingUser = $userRepository->findOneBy(['email' => $email]);
                
                if ($existingUser) {
                    $error = 'Cette adresse email est déjà utilisée.';
                } else {
                    try {
                        // Créer l'utilisateur
                        $user = new User();
                        $user->setFirstname($firstname);
                        $user->setLastname($lastname);
                        $user->setEmail($email);
                        
                        // Hasher le mot de passe
                        $hashedPassword = $this->passwordHasher->hashPassword($user, $password);
                        $user->setPasswordhash($hashedPassword);
                        
                        // Définir le numéro de téléphone s'il est fourni
                        if (!empty($phoneNumber)) {
                            $user->setPhonenumber($phoneNumber);
                        }
                        
                        // Définir le rôle comme ROLE_USER par défaut
                        $user->setRole('ROLE_USER');
                        $user->setIsactive(true);
                        $user->setViolationcount(0);
                        $user->setSubscriptionenddate(new \DateTime('+1 month'));
                        $user->setCreatedat(new \DateTime());
                        $user->setUpdatedat(new \DateTime());
                        
                        // Sauvegarder dans la base de données
                        $this->entityManager->persist($user);
                        $this->entityManager->flush();
                        
                        // Connexion automatique de l'utilisateur
                        $session = $this->requestStack->getSession();
                        $session->set('user_id', $user->getId());
                        $session->set('user_role', $user->getRole());
                        $session->set('user_name', $user->getFirstname() . ' ' . $user->getLastname());
                        
                        $this->addFlash('success', 'Votre compte a été créé avec succès!');
                        
                        // Rediriger vers la page d'accueil front
                        return $this->redirectToRoute('app_front_homepage');
                    } catch (\Exception $e) {
                        // Log l'erreur pour le débogage
                        error_log($e->getMessage());
                        $error = 'Une erreur est survenue lors de la création du compte.';
                    }
                }
            }
        }
        
        return $this->render('front/register.html.twig', [
            'error' => $error,
            'success' => $success,
        ]);
    }
    
    #[Route('/front', name: 'app_front_homepage')]
    public function frontHomepage(): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur complet
        $user = $this->getUserFromSession();
        
        return $this->render('front/homepage.html.twig', [
            'user' => $user,
            'user_name' => $session->get('user_name'),
            'user_role' => $session->get('user_role'),
        ]);
    }
    
    #[Route('/front/profile', name: 'app_front_profile')]
    public function profile(): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur complet
        $user = $this->getUserFromSession();
        
        // Récupérer les salles de l'utilisateur
        $salleRepository = $this->entityManager->getRepository(SalleDeSport::class);
        $userSalles = $salleRepository->findBy(['user' => $user]);
        
        // Récupérer les équipements de l'utilisateur
        $equipementRepository = $this->entityManager->getRepository(Equipement::class);
        $userEquipements = $equipementRepository->findBy(['user' => $user], ['id' => 'DESC'], 5);
        
        return $this->render('front/profile.html.twig', [
            'user' => $user,
            'user_salles' => $userSalles,
            'user_equipements' => $userEquipements,
        ]);
    }
    
    #[Route('/admin', name: 'app_admin_dashboard')]
    public function adminDashboard(): Response
    {
        // Vérifier si l'utilisateur est connecté et est un admin
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Rediriger vers la liste des utilisateurs
        return $this->redirectToRoute('app_user_index');
    }
    
    #[Route('/admin/users', name: 'app_admin_users')]
    public function adminUsers(): Response
    {
        // Vérifier si l'utilisateur est connecté et est un admin
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Rediriger vers la gestion des utilisateurs
        return $this->redirectToRoute('app_user_index');
    }
    
    #[Route('/user/list', name: 'app_user_index', methods: ['GET'])]
    public function index(): Response
    {
        // Vérifier les droits d'administration
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        $users = $this->entityManager
            ->getRepository(User::class)
            ->findAll();

        return $this->render('user/index.html.twig', [
            'users' => $users,
        ]);
    }

    #[Route('/user/new', name: 'app_user_new', methods: ['GET', 'POST'])]
    public function new(Request $request): Response
    {
        // Vérifier les droits d'administration
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        $user = new User();
        $form = $this->createForm(UserType::class, $user);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                // S'assurer que les dates sont définies
                if (!$user->getCreatedat()) {
                    $user->setCreatedat(new \DateTime());
                }
                if (!$user->getUpdatedat()) {
                    $user->setUpdatedat(new \DateTime());
                }
                
                $this->entityManager->persist($user);
                $this->entityManager->flush();

                $this->addFlash('success', 'Utilisateur créé avec succès!');
                return $this->redirectToRoute('app_user_index', [], Response::HTTP_SEE_OTHER);
            } catch (\Exception $e) {
                $this->addFlash('error', 'Erreur lors de la création: ' . $e->getMessage());
            }
        }

        return $this->render('user/new.html.twig', [
            'user' => $user,
            'form' => $form,
        ]);
    }

    #[Route('/user/{id}', name: 'app_user_show', methods: ['GET'])]
    public function show(User $user): Response
    {
        // Vérifier les droits d'administration
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        return $this->render('user/show.html.twig', [
            'user' => $user,
        ]);
    }

    #[Route('/user/{id}/edit', name: 'app_user_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, User $user): Response
    {
        // Vérifier les droits d'administration
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Sauvegarder le mot de passe actuel
        $originalPassword = $user->getPasswordhash();
        
        // Créer le formulaire avec l'option is_edit à true
        $form = $this->createForm(UserType::class, $user, ['is_edit' => true]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                // Si le mot de passe est vide, restaurer l'original
                if (empty($user->getPasswordhash())) {
                    $user->setPasswordhash($originalPassword);
                } else {
                    // Si un nouveau mot de passe a été fourni, le hasher
                    $hashedPassword = $this->passwordHasher->hashPassword($user, $user->getPasswordhash());
                    $user->setPasswordhash($hashedPassword);
                }
                
                // Mettre à jour la date de mise à jour
                $user->setUpdatedat(new \DateTime());
                
                $this->entityManager->flush();
                
                $this->addFlash('success', 'Utilisateur modifié avec succès!');
                return $this->redirectToRoute('app_user_index', [], Response::HTTP_SEE_OTHER);
            } catch (\Exception $e) {
                $this->addFlash('error', 'Erreur lors de la modification: ' . $e->getMessage());
            }
        }

        return $this->render('user/edit.html.twig', [
            'user' => $user,
            'form' => $form,
        ]);
    }

    #[Route('/user/{id}/delete', name: 'app_user_delete', methods: ['POST'])]
    public function delete(Request $request, User $user): Response
    {
        // Vérifier les droits d'administration
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        if ($this->isCsrfTokenValid('delete'.$user->getId(), $request->getPayload()->getString('_token'))) {
            $this->entityManager->remove($user);
            $this->entityManager->flush();
            
            $this->addFlash('success', 'Utilisateur supprimé avec succès!');
        }

        return $this->redirectToRoute('app_user_index', [], Response::HTTP_SEE_OTHER);
    }
    
    #[Route('/front/profile/edit', name: 'app_edit_profile')]
    public function editProfile(Request $request): Response
    {
        // Vérifier si l'utilisateur est connecté
        $session = $this->requestStack->getSession();
        if (!$session->has('user_id')) {
            $this->addFlash('error', 'Vous devez être connecté pour modifier votre profil.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur connecté
        $user = $this->getUserFromSession();
        if (!$user) {
            $this->addFlash('error', 'Utilisateur non trouvé.');
            return $this->redirectToRoute('app_login');
        }
        
        // Sauvegarder le mot de passe actuel
        $originalPassword = $user->getPasswordhash();
        
        // Créer le formulaire avec l'option is_edit à true
        $form = $this->createForm(UserType::class, $user, [
            'is_edit' => true,
            'front_edit' => true, // Option pour masquer certains champs dans le frontend
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            try {
                // Si le mot de passe est vide, restaurer l'original
                if (empty($user->getPasswordhash())) {
                    $user->setPasswordhash($originalPassword);
                } else {
                    // Si un nouveau mot de passe a été fourni, le hasher
                    $hashedPassword = $this->passwordHasher->hashPassword($user, $user->getPasswordhash());
                    $user->setPasswordhash($hashedPassword);
                }
                
                // Mettre à jour la date de mise à jour
                $user->setUpdatedat(new \DateTime());
                
                $this->entityManager->flush();
                
                $this->addFlash('success', 'Votre profil a été modifié avec succès!');
                return $this->redirectToRoute('app_front_profile');
            } catch (\Exception $e) {
                $this->addFlash('error', 'Erreur lors de la modification: ' . $e->getMessage());
            }
        }

        return $this->render('front/edit_profile.html.twig', [
            'user' => $user,
            'form' => $form,
        ]);
    }
    
    #[Route('/test/users', name: 'app_test_users')]
    public function testUsers(): Response
    {
        // Vérifier si l'utilisateur est admin
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer tous les utilisateurs
        $users = $this->entityManager->getRepository(User::class)->findAll();
        
        $usersList = [];
        foreach ($users as $user) {
            $usersList[] = [
                'id' => $user->getId(),
                'email' => $user->getEmail(),
                'role' => $user->getRole(),
                'isActive' => $user->getIsactive(),
            ];
        }
        
        // Retourner les utilisateurs au format JSON pour debug
        return new Response('<pre>' . json_encode($usersList, JSON_PRETTY_PRINT) . '</pre>');
    }
    
    #[Route('/test/login/{email}/{password}', name: 'app_test_login')]
    public function testLogin(string $email, string $password): Response
    {
        // Vérifier si l'utilisateur est admin
        if (!$this->getUserFromSession() || !$this->checkUserRole('ROLE_ADMIN')) {
            $this->addFlash('error', 'Accès refusé. Vous devez être administrateur pour accéder à cette section.');
            return $this->redirectToRoute('app_login');
        }
        
        // Récupérer l'utilisateur par email
        $user = $this->entityManager->getRepository(User::class)->findOneBy(['email' => $email]);
        
        if (!$user) {
            return new Response('Utilisateur non trouvé avec l\'email: ' . $email);
        }
        
        // Vérifier si le mot de passe correspond avec le hasher
        $passwordMatch = $this->passwordHasher->isPasswordValid($user, $password);
        
        // Créer la session si le mot de passe correspond
        if ($passwordMatch) {
            $session = $this->requestStack->getSession();
            $session->set('user_id', $user->getId());
            $session->set('user_role', $user->getRole());
            $session->set('user_name', $user->getFirstname() . ' ' . $user->getLastname());
            
            return new Response('Connexion réussie pour ' . $email . '. Session créée.');
        }
        
        return new Response('Échec de connexion pour ' . $email . '. Mot de passe incorrect.');
    }
    
    private function getUserFromSession()
    {
        $session = $this->requestStack->getSession();
        $userId = $session->get('user_id');
        
        if ($userId) {
            return $this->entityManager->getRepository(User::class)->find($userId);
        }
        
        return null;
    }
    
    private function checkUserRole(string $role): bool
    {
        $session = $this->requestStack->getSession();
        $userRole = $session->get('user_role');
        
        if ($userRole === $role) {
            return true;
        }
        
        // L'administrateur a accès à tous les rôles
        if ($userRole === 'ROLE_ADMIN') {
            return true;
        }
        
        return false;
    }
}

class GymController extends AbstractController
{
    #[Route('/gym', name: 'app_gym')]
    public function index(SessionInterface $session)
    {
        // Récupérer l'utilisateur connecté depuis la session
        $userId = $session->get('user_id');

        if (!$userId) {
            // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            return $this->redirectToRoute('app_login');
        }

        // Exemple : Récupérer des informations spécifiques liées à la salle de sport
        $gymName = $session->get('gym_name', 'Salle de sport par défaut');

        return $this->render('gym/index.html.twig', [
            'gym_name' => $gymName,
        ]);
    }

    #[Route('/gym/set', name: 'app_gym_set')]
    public function setGym(SessionInterface $session, Request $request)
    {
        // Exemple : Définir une salle de sport dans la session
        $gymName = $request->get('gym_name', 'Salle de sport par défaut');
        $session->set('gym_name', $gymName);

        $this->addFlash('success', 'La salle de sport a été mise à jour dans la session.');

        return $this->redirectToRoute('app_gym');
    }
}
