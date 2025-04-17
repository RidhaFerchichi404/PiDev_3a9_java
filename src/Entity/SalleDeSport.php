<?php

namespace App\Entity;

use App\Repository\SalleDeSportRepository;
use App\Validator\ZoneInRegion;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: SalleDeSportRepository::class)]
class SalleDeSport
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;
    #[ORM\OneToMany(mappedBy: 'salle', targetEntity: Equipement::class)]
    private Collection $equipements;
    
    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le nom ne doit pas être vide.")]
    #[Assert\Length(
        min: 2, 
        max: 100, 
        minMessage: "Le nom doit contenir au moins {{ limit }} caractères.", 
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-ZÀ-ÿ0-9\s\-']+$/", 
        message: "Le nom ne peut contenir que des lettres, chiffres, espaces, tirets et apostrophes."
    )]
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "La zone ne doit pas être vide.")]
   
    #[ZoneInRegion]
    private ?string $zone = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "La région est requise.")]
  
    private ?string $region = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le nom de l'image est requis.")]
    #[Assert\Regex(
        pattern: "/^.+\.(jpg|jpeg|png|gif|webp)$/i",
        message: "Le fichier doit être une image (jpg, jpeg, png, gif ou webp)."
    )]
    #[Assert\Length(
        max: 255, 
        maxMessage: "Le nom de l'image ne peut pas dépasser {{ limit }} caractères."
    )]
    private ?string $image = null;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "id_user", referencedColumnName: "id", nullable: false)]
    #[Assert\NotNull(message: "L'utilisateur est requis.")]
    private ?User $user = null;

    public function __construct()
    {
        $this->equipements = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getZone(): ?string
    {
        return $this->zone;
    }

    public function setZone(string $zone): static
    {
        $this->zone = $zone;

        return $this;
    }

    public function getRegion(): ?string
    {
        return $this->region;
    }

    public function setRegion(string $region): static
    {
        $this->region = $region;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(string $image): static
    {
        $this->image = $image;

        return $this;
    }

    public function getIdUser(): ?int
    {
        return $this->user ? $this->user->getId() : null;
    }

    public function setIdUser(int $id_user): static
    {
        $this->id_user = $id_user;

        return $this;
    }
    
    public function getUser(): ?User
    {
        return $this->user;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;
        return $this;
    }

    public function getEquipements(): Collection
    {
        return $this->equipements;
    }
}
