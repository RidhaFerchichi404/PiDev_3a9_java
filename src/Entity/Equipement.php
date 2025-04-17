<?php

namespace App\Entity;

use App\Repository\EquipementRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: EquipementRepository::class)]
#[Assert\Expression(
    "this.getDerniereVerification() <= this.getProchaineVerification()",
    message: "La date de dernière vérification doit être antérieure ou égale à la prochaine vérification."
)]
class Equipement
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\ManyToOne(inversedBy: 'equipements')]
    #[ORM\JoinColumn(name: "id_salle", referencedColumnName: "id", nullable: false)]
    #[Assert\NotNull(message: "La salle doit être sélectionnée.")]
    private ?SalleDeSport $salle = null;

    #[ORM\Column]
    #[Assert\NotNull(message: "L'utilisateur est requis.")]
    #[Assert\Positive(message: "L'identifiant utilisateur doit être un entier positif.")]
    private ?int $id_user = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Le nom est requis.")]
    #[Assert\Length(
        min: 3, 
        max: 50, 
        minMessage: "Le nom doit contenir au moins {{ limit }} caractères.",
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    #[Assert\Regex(
        pattern: "/^[a-zA-Z0-9àáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆŠŽ ,.'-]+$/",
        message: "Le nom ne doit contenir que des lettres, chiffres et certains caractères spéciaux."
    )]
    private ?string $nom = null;

    #[ORM\Column]
    #[Assert\NotNull(message: "Le champ fonctionnement est requis.")]
    #[Assert\Type(type: "bool", message: "La valeur doit être un booléen (vrai/faux).")]
    private ?bool $fonctionnement = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotNull(message: "La date de prochaine vérification est requise.")]
    #[Assert\Type(type: \DateTimeInterface::class, message: "La date n'est pas valide.")]
    #[Assert\GreaterThanOrEqual(
        value: "today",
        message: "La date de prochaine vérification doit être aujourd'hui ou dans le futur."
    )]
    private ?\DateTimeInterface $prochaine_verification = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotNull(message: "La date de dernière vérification est requise.")]
    #[Assert\Type(type: \DateTimeInterface::class, message: "La date n'est pas valide.")]
    #[Assert\LessThanOrEqual(
        value: "today",
        message: "La date de dernière vérification doit être aujourd'hui ou dans le passé."
    )]
    private ?\DateTimeInterface $derniere_verification = null;

    #[ORM\OneToMany(mappedBy: 'equipement', targetEntity: Exercice::class, cascade: ['persist', 'remove'])]
    private Collection $exercices;

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

    public function __construct()
    {
        $this->exercices = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getSalle(): ?SalleDeSport
    {
        return $this->salle;
    }

    public function setSalle(?SalleDeSport $salle): static
    {
        $this->salle = $salle;
        return $this;
    }

    public function getIdUser(): ?int
    {
        return $this->id_user;
    }

    public function setIdUser(int $id_user): static
    {
        $this->id_user = $id_user;
        return $this;
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

    public function isFonctionnement(): ?bool
    {
        return $this->fonctionnement;
    }

    public function setFonctionnement(bool $fonctionnement): static
    {
        $this->fonctionnement = $fonctionnement;
        return $this;
    }

    public function getProchaineVerification(): ?\DateTimeInterface
    {
        return $this->prochaine_verification;
    }

    public function setProchaineVerification(\DateTimeInterface $prochaine_verification): static
    {
        $this->prochaine_verification = $prochaine_verification;
        return $this;
    }

    public function getDerniereVerification(): ?\DateTimeInterface
    {
        return $this->derniere_verification;
    }

    public function setDerniereVerification(\DateTimeInterface $derniere_verification): static
    {
        $this->derniere_verification = $derniere_verification;
        return $this;
    }

    /**
     * Get the User object associated with this equipment
     */
    public function getUser(): ?User
    {
        // Return null if no id_user or no entityManager injected
        if ($this->id_user === null || $this->entityManager === null) {
            return null;
        }
        
        // Use the injected entity manager to get the User by id
        return $this->entityManager->getRepository(User::class)->find($this->id_user);
    }

    /**
     * @return Collection<int, Exercice>
     */
    public function getExercices(): Collection
    {
        return $this->exercices;
    }

    public function addExercice(Exercice $exercice): static
    {
        if (!$this->exercices->contains($exercice)) {
            $this->exercices->add($exercice);
            $exercice->setEquipement($this);
        }

        return $this;
    }

    public function removeExercice(Exercice $exercice): static
    {
        if ($this->exercices->removeElement($exercice)) {
            // set the owning side to null (unless already changed)
            if ($exercice->getEquipement() === $this) {
                $exercice->setEquipement(null);
            }
        }

        return $this;
    }
}
