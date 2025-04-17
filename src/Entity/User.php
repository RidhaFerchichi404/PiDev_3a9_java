<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;

#[ORM\Entity]
#[ORM\HasLifecycleCallbacks]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{

    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: "integer")]
    private ?int $id = null;

    #[ORM\Column(type: "string", length: 255)]
    #[Assert\NotBlank(message: "Le prénom est obligatoire.")]
    #[Assert\Length(
        max: 255,
        maxMessage: "Le prénom ne peut pas dépasser {{ limit }} caractères."
    )]
    private string $first_name;

    #[ORM\Column(type: "string", length: 255)]
    #[Assert\NotBlank(message: "Le nom est obligatoire.")]
    #[Assert\Length(
        max: 255,
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    private string $last_name;

    #[ORM\Column(type: "string", length: 255, unique: true)]
    #[Assert\NotBlank(message: "L'email est obligatoire.")]
    #[Assert\Email(message: "L'email '{{ value }}' n'est pas valide.")]
    private string $email;

    #[ORM\Column(type: "string", length: 255)]
    #[Assert\NotBlank(message: "Le mot de passe est obligatoire.")]
    #[Assert\Length(
        min: 8,
        minMessage: "Le mot de passe doit contenir au moins {{ limit }} caractères."
    )]
    private string $password_hash;

    #[ORM\Column(type: "string", length: 20, nullable: true)]
    #[Assert\Regex(
        pattern: "/^\+?[0-9]{10,20}$/",
        message: "Le numéro de téléphone doit être valide."
    )]
    private ?string $phone_number = null;

    #[ORM\Column(type: "string", length: 50)]
    #[Assert\Choice(
        choices: ["ROLE_USER", "ROLE_CLIENT", "ROLE_COACH", "ROLE_ADMIN"],
        message: "Le rôle doit être 'ROLE_USER', 'ROLE_CLIENT', 'ROLE_COACH' ou 'ROLE_ADMIN'."
    )]
    private string $role = 'ROLE_USER';

    #[ORM\Column(type: "date")]
    #[Assert\NotBlank(message: "La date de fin d'abonnement est obligatoire.")]
    #[Assert\Type("\DateTimeInterface", message: "La date doit être valide.")]
    private \DateTimeInterface $subscription_end_date;

    #[ORM\Column(type: "boolean")]
    private bool $is_active = true;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $created_at;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $updated_at;

    #[ORM\Column(type: "integer")]
    private int $violation_count = 0;

    #[ORM\Column(type: "string", length: 255, nullable: true)]
    #[Assert\Length(
        max: 255,
        maxMessage: "La localisation ne peut pas dépasser {{ limit }} caractères."
    )]
    private ?string $location = null;

    #[ORM\Column(type: "string", length: 20, nullable: true)]
    #[Assert\Regex(
        pattern: "/^[0-9]{8}$/",
        message: "Le CIN doit contenir exactement 8 chiffres."
    )]
    private ?string $cin = null;

    #[ORM\Column(type: "integer", nullable: true)]
    #[Assert\PositiveOrZero(message: "L'âge doit être un nombre positif ou nul.")]
    private ?int $age = null;

    public function __construct()
    {
        // Initialisation des dates
        $this->created_at = new \DateTime();
        $this->updated_at = new \DateTime();
        
        // Date d'abonnement par défaut à un mois
        $this->subscription_end_date = new \DateTime('+1 month');
    }

    #[ORM\PrePersist]
    public function setCreatedAtValue(): void
    {
        $this->created_at = new \DateTime();
        $this->updated_at = new \DateTime();
    }

    #[ORM\PreUpdate]
    public function setUpdatedAtValue(): void
    {
        $this->updated_at = new \DateTime();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getFirstname(): string
    {
        return $this->first_name;
    }

    public function setFirstname(string $value): self
    {
        $this->first_name = $value;
        return $this;
    }

    public function getLastname(): string
    {
        return $this->last_name;
    }

    public function setLastname(string $value): self
    {
        $this->last_name = $value;
        return $this;
    }

    public function getEmail(): string
    {
        return $this->email;
    }

    public function setEmail(string $value): self
    {
        $this->email = $value;
        return $this;
    }

    public function getPasswordhash(): string
    {
        return $this->password_hash;
    }

    public function setPasswordhash(string $value): self
    {
        $this->password_hash = $value;
        return $this;
    }

    public function getPhonenumber(): ?string
    {
        return $this->phone_number;
    }

    public function setPhonenumber(?string $value): self
    {
        $this->phone_number = $value;
        return $this;
    }

    public function getRole(): string
    {
        return $this->role;
    }

    public function setRole(string $value): self
    {
        $this->role = $value;
        return $this;
    }

    public function getSubscriptionenddate(): \DateTimeInterface
    {
        return $this->subscription_end_date;
    }

    public function setSubscriptionenddate(\DateTimeInterface $value): self
    {
        $this->subscription_end_date = $value;
        return $this;
    }

    public function getIsactive(): bool
    {
        return $this->is_active;
    }

    public function setIsactive(bool $value): self
    {
        $this->is_active = $value;
        return $this;
    }

    public function getCreatedat(): \DateTimeInterface
    {
        return $this->created_at;
    }

    public function setCreatedat(\DateTimeInterface $value): self
    {
        $this->created_at = $value;
        return $this;
    }

    public function getUpdatedat(): \DateTimeInterface
    {
        return $this->updated_at;
    }

    public function setUpdatedat(\DateTimeInterface $value): self
    {
        $this->updated_at = $value;
        return $this;
    }

    public function getViolationcount(): int
    {
        return $this->violation_count;
    }

    public function setViolationcount(int $value): self
    {
        $this->violation_count = $value;
        return $this;
    }

    public function getLocation(): ?string
    {
        return $this->location;
    }

    public function setLocation(?string $value): self
    {
        $this->location = $value;
        return $this;
    }

    public function getCin(): ?string
    {
        return $this->cin;
    }

    public function setCin(?string $value): self
    {
        $this->cin = $value;
        return $this;
    }

    public function getAge(): ?int
    {
        return $this->age;
    }

    public function setAge(?int $value): self
    {
        $this->age = $value;
        return $this;
    }
    
    public function getFullName(): string
    {
        return $this->first_name . ' ' . $this->last_name;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        return [$this->role];
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): string
    {
        return $this->password_hash;
    }

    /**
     * Method to check if a plain password matches this user's password
     */
    public function checkPassword(string $plainPassword): bool
    {
        // Ne pas faire de comparaison directe ici, cela sera géré par le UserPasswordHasherInterface
        return false;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
    }

    /**
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return $this->email;
    }
}
