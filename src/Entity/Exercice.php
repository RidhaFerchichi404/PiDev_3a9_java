<?php

namespace App\Entity;

use App\Repository\ExerciceRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use App\Entity\Equipement;
use App\Entity\User;

#[ORM\Entity(repositoryClass: ExerciceRepository::class)]
class Exercice
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "La description est requise.")]
    #[Assert\Length(
        min: 10, 
        max: 255, 
        minMessage: "La description doit contenir au moins {{ limit }} caractères.",
        maxMessage: "La description ne peut pas dépasser {{ limit }} caractères."
    )]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z0-9àáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆŠŽ ,.\'"-]+$/',
        message: "La description ne doit contenir que des lettres, chiffres et certains caractères spéciaux."
    )]
    private ?string $description = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "L'image est requise.")]
    #[Assert\Regex(
        pattern: "/\\.(gif|jpe?g|png|webp)$/i",
        message: "Le fichier doit être au format GIF, JPEG, PNG ou WEBP."
    )]
    private ?string $image = null;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[ORM\JoinColumn(name: "id_user", referencedColumnName: "id", nullable: false)]
    #[Assert\NotNull(message: "L'utilisateur est requis.")]
    private ?User $user = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le nom de l'exercice est requis.")]
    #[Assert\Length(
        min: 3, 
        max: 50, 
        minMessage: "Le nom de l'exercice doit contenir au moins {{ limit }} caractères.",
        maxMessage: "Le nom de l'exercice ne peut pas dépasser {{ limit }} caractères."
    )]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z0-9àáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆŠŽ ,.\'"-]+$/',
        message: "Le nom de l'exercice ne doit contenir que des lettres, chiffres et certains caractères spéciaux."
    )]
    private ?string $nom_exercice = null;

    #[ORM\ManyToOne(targetEntity: Equipement::class, inversedBy: 'exercices')]
    #[ORM\JoinColumn(name: 'id_equipement', referencedColumnName: 'id', nullable: false)]
    #[Assert\NotNull(message: "L'équipement est obligatoire.")]
    private ?Equipement $equipement = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Assert\Choice(
        choices: ["biceps", "triceps", "épaules", "dos", "pectoraux", "abdominaux", "jambes", "fessiers", "mollets", "avant-bras", "cardio", "full-body"],
        message: "Le groupe musculaire choisi n'est pas valide.",
        multiple: false
    )]
    private ?string $muscle = null;

    #[ORM\Column(length: 1000, nullable: true)]
    #[Assert\Length(
        max: 1000,
        maxMessage: "Les conseils ne peuvent pas dépasser {{ limit }} caractères."
    )]
    private ?string $conseils = null;
    
    /**
     * Temporary storage for entity manager (not persisted)
     */
    private $entityManager = null;
    
    /**
     * Setter for entity manager (should be called by controller)
     */
    public function setEntityManager($em): self
    {
        $this->entityManager = $em;
        return $this;
    }
    
    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;
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
        if ($this->entityManager !== null) {
            $user = $this->entityManager->getRepository(User::class)->find($id_user);
            if ($user) {
                $this->user = $user;
            }
        }
        return $this;
    }

    public function getUser(): ?User
    {
        // If we already have a loaded User entity, return it
        if ($this->user !== null) {
            return $this->user;
        }
        
        // If we have an id_user but no loaded entity, and entityManager is available, load the User
        $id_user = $this->getIdUser();
        if ($id_user !== null && $this->entityManager !== null) {
            return $this->entityManager->getRepository(User::class)->find($id_user);
        }
        
        // Otherwise return null
        return null;
    }

    public function setUser(?User $user): static
    {
        $this->user = $user;
        return $this;
    }

    public function getNomExercice(): ?string
    {
        return $this->nom_exercice;
    }

    public function setNomExercice(string $nom_exercice): static
    {
        $this->nom_exercice = $nom_exercice;
        return $this;
    }

    public function getEquipement(): ?Equipement
    {
        return $this->equipement;
    }
    
    public function setEquipement(?Equipement $equipement): static
    {
        $this->equipement = $equipement;
        return $this;
    }

    public function getMuscle(): ?string
    {
        return $this->muscle;
    }

    public function setMuscle(?string $muscle): static
    {
        $this->muscle = $muscle;
        return $this;
    }

    public function getConseils(): ?string
    {
        return $this->conseils;
    }

    public function setConseils(?string $conseils): static
    {
        $this->conseils = $conseils;
        return $this;
    }
}
