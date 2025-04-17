<?php

namespace App\Form;

use App\Entity\SalleDeSport;
use App\Service\RegionService;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\HiddenType;
use Symfony\Component\Form\Extension\Core\Type\EntityType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Symfony\Component\Form\FormInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\CallbackTransformer;
use App\Entity\User;

class SalleDeSportType extends AbstractType
{
    private RegionService $regionService;

    public function __construct(RegionService $regionService)
    {
        $this->regionService = $regionService;
    }

    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        // Récupérer les données depuis les options
        $data = $options['data'] ?? null;
        $currentRegion = $data ? $data->getRegion() : null;
        $choices = [];
        
        // Debug - Afficher les informations actuelles
        if ($currentRegion) {
            echo "<!-- DEBUG: Région actuelle: {$currentRegion} -->";
            
            $allRegions = $this->regionService->getAllRegions();
            $matchingRegion = null;
            foreach ($allRegions as $region) {
                if (strtolower($region) === strtolower($currentRegion)) {
                    $matchingRegion = $region;
                    break;
                }
            }
            
            if ($matchingRegion && $matchingRegion !== $currentRegion) {
                echo "<!-- DEBUG: Correspondance trouvée avec une casse différente: {$matchingRegion} -->";
                // Corriger la casse pour éviter les problèmes
                $currentRegion = $matchingRegion;
            }
        }
        
        // Préparer les choix de zones si une région est déjà sélectionnée
        if ($currentRegion) {
            $zones = $this->regionService->getZonesByRegion($currentRegion);
            
            if (!empty($zones)) {
                $choices = array_combine($zones, $zones);
                echo "<!-- DEBUG: Zones trouvées: " . implode(', ', $zones) . " -->";
            } else {
                echo "<!-- DEBUG: Aucune zone trouvée pour la région {$currentRegion} -->";
            }
        }
        
        // Pour le debug, on stocke aussi toutes les régions disponibles
        $regionsData = json_encode($this->regionService->getAllRegionsWithZones());
        
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom de la salle',
                'attr' => [
                    'placeholder' => 'Entrez le nom de la salle',
                    'class' => 'form-control'
                ]
            ])
            ->add('region', ChoiceType::class, [
                'label' => 'Région',
                'choices' => $this->getRegionChoices(),
                'placeholder' => 'Choisissez une région',
                'attr' => [
                    'class' => 'form-control region-select',
                ],
                'required' => true,
                'choice_translation_domain' => false,
            ])
            ->add('zone', ChoiceType::class, [
                'label' => 'Ville',
                'choices' => $choices,
                'placeholder' => $currentRegion ? 'Choisissez une ville dans ' . $currentRegion : 'Choisissez d\'abord une région',
                'attr' => [
                    'class' => 'form-control zone-select',
                ],
                'required' => true,
            ])
            ->add('image', TextType::class, [
                'label' => 'Image',
                'attr' => [
                    'placeholder' => 'Nom du fichier image',
                    'class' => 'form-control'
                ]
            ]);
        
        // Ajouter le champ id_user seulement si hide_user n'est pas à true
        if (!isset($options['hide_user']) || $options['hide_user'] === false) {
            $builder->add('user', EntityType::class, [
                'class' => User::class,
                'choice_label' => function ($user) {
                    return $user->getFirstname() . ' ' . $user->getLastname();
                },
                'label' => 'Propriétaire',
                'attr' => [
                    'class' => 'form-control'
                ],
                'required' => true
            ]);
        }
            
        // Ajouter un écouteur pour gérer le pré-soumission du formulaire
        $builder->addEventListener(FormEvents::PRE_SUBMIT, function (FormEvent $event) {
            $data = $event->getData();
            $form = $event->getForm();
            
            if (!isset($data['region']) || empty($data['region'])) {
                return;
            }
            
            $region = $data['region'];
            $zones = $this->regionService->getZonesByRegion($region);
            
            if (!empty($zones)) {
                $choices = array_combine($zones, $zones);
                $form->add('zone', ChoiceType::class, [
                    'label' => 'Ville',
                    'choices' => $choices,
                    'placeholder' => "Choisissez une ville dans {$region}",
                    'attr' => [
                        'class' => 'form-control zone-select',
                    ],
                    'required' => true,
                ]);
            }
        });
        
        // Ajouter un écouteur pour mettre à jour les choix de villes lorsque la région change
        $builder->get('region')->addEventListener(FormEvents::POST_SUBMIT, function (FormEvent $event) {
            $form = $event->getForm();
            $region = $form->getData();
            
            if (!$region) {
                return;
            }
            
            $parentForm = $form->getParent();
            $this->addZoneField($parentForm, $region);
        });
        
        // Ajouter un bouton caché qui se soumet automatiquement lorsque la région change
        $builder->add('autoSubmit', HiddenType::class, [
            'mapped' => false,
            'attr' => [
                'class' => 'auto-submit-field',
            ],
            'required' => false,
        ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => SalleDeSport::class,
            'allow_extra_fields' => true,
            'error_bubbling' => false,
            'hide_user' => false,
            // Ajout du flag pour activer le changement de formulaire en AJAX
            'attr' => [
                'class' => 'auto-submit-form'
            ]
        ]);
    }

    private function getRegionChoices(): array
    {
        $regions = $this->regionService->getAllRegions();
        return array_combine($regions, $regions);
    }

    /**
     * Ajoute le champ "zone" avec les villes correspondantes à la région
     */
    private function addZoneField(FormInterface $form, string $region): void
    {
        $zones = $this->regionService->getZonesByRegion($region);
        $choices = !empty($zones) ? array_combine($zones, $zones) : [];
        
        $form->add('zone', ChoiceType::class, [
            'label' => 'Ville',
            'choices' => $choices,
            'placeholder' => "Choisissez une ville dans {$region}",
            'attr' => [
                'class' => 'form-control zone-select',
            ],
            'required' => true,
        ]);
    }
}
