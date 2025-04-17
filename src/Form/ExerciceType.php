<?php

namespace App\Form;

use App\Entity\Equipement;
use App\Entity\Exercice;
use App\Entity\User;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; 
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;

class ExerciceType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom_exercice', TextType::class, [
                'label' => 'Nom de l\'exercice',
                'attr' => [
                    'placeholder' => 'Entrez le nom de l\'exercice',
                    'minlength' => 3,
                    'maxlength' => 50,
                ],
                'help' => 'Entrez un nom clair et descriptif (3-50 caractères)'
            ])
            ->add('description', TextareaType::class, [
                'label' => 'Description',
                'attr' => [
                    'placeholder' => 'Décrivez l\'exercice en détail',
                    'rows' => 4,
                    'minlength' => 10,
                    'maxlength' => 255,
                ],
                'help' => 'Décrivez comment réaliser l\'exercice correctement (10-255 caractères)'
            ])
            ->add('image', TextType::class, [
                'label' => 'Image (GIF, JPEG, PNG, WEBP)',
                'attr' => [
                    'placeholder' => 'Entrez le nom du fichier image (ex: exercice.gif)',
                ],
                'help' => 'L\'image doit être au format GIF, JPEG, PNG ou WEBP'
            ])
            ->add('muscle', ChoiceType::class, [
                'label' => 'Groupe musculaire ciblé',
                'choices' => [
                    'Membres supérieurs' => [
                        'Biceps' => 'biceps',
                        'Triceps' => 'triceps',
                        'Épaules' => 'épaules',
                        'Avant-bras' => 'avant-bras',
                    ],
                    'Tronc' => [
                        'Pectoraux' => 'pectoraux',
                        'Dos' => 'dos',
                        'Abdominaux' => 'abdominaux',
                    ],
                    'Membres inférieurs' => [
                        'Jambes' => 'jambes',
                        'Fessiers' => 'fessiers',
                        'Mollets' => 'mollets',
                    ],
                    'Autre' => [
                        'Cardio' => 'cardio',
                        'Full-body' => 'full-body',
                    ],
                ],
                'placeholder' => 'Choisissez un groupe musculaire',
                'required' => false,
            ])
            ->add('conseils', TextareaType::class, [
                'label' => 'Conseils et astuces',
                'attr' => [
                    'placeholder' => 'Conseils pour améliorer l\'efficacité de l\'exercice',
                    'rows' => 5,
                    'maxlength' => 1000,
                ],
                'required' => false,
                'help' => 'Ajoutez des astuces pour maximiser les résultats (max. 1000 caractères)'
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
            
        // Ajouter le champ equipement seulement si hide_equipement n'est pas à true
        if (!isset($options['hide_equipement']) || $options['hide_equipement'] === false) {
            $builder->add('equipement', EntityType::class, [
                'class' => Equipement::class,
                'choice_label' => 'nom', // Afficher le nom au lieu de l'ID
                'placeholder' => 'Choisir un équipement',
                'required' => true,
                'attr' => [
                    'class' => 'select2-control', // Pour permettre une sélection améliorée avec JS
                ],
                'help' => 'Sélectionnez l\'équipement utilisé pour cet exercice',
                'query_builder' => function ($er) {
                    return $er->createQueryBuilder('e')
                        ->where('e.fonctionnement = :val')
                        ->setParameter('val', true)
                        ->orderBy('e.nom', 'ASC');
                },
            ]);
        }
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Exercice::class,
            'hide_equipement' => false,
            'hide_user' => false,
        ]);
    }
}
