<?php

namespace App\Form;

use App\Entity\Equipement;
use App\Entity\SalleDeSport;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class EquipementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'label' => 'Nom de l\'équipement',
                'attr' => [
                    'placeholder' => 'Entrez le nom de l\'équipement',
                    'minlength' => 3,
                    'maxlength' => 50,
                    'class' => 'form-control-lg',
                ],
                'help' => 'Entrez un nom descriptif (3-50 caractères)'
            ])
            ->add('fonctionnement', CheckboxType::class, [
                'label' => 'L\'équipement est-il fonctionnel ?',
                'required' => true,
                'help' => 'Cochez la case si l\'équipement est en bon état de fonctionnement',
                'label_attr' => [
                    'class' => 'form-check-label'
                ],
                'attr' => [
                    'class' => 'form-check-input'
                ]
            ])
            ->add('prochaine_verification', DateType::class, [
                'label' => 'Date de prochaine vérification',
                'widget' => 'single_text',
                'attr' => [
                    'min' => (new \DateTime())->format('Y-m-d'),
                    'class' => 'form-control-lg',
                ],
                'help' => 'La date doit être aujourd\'hui ou dans le futur',
            ])
            ->add('derniere_verification', DateType::class, [
                'label' => 'Date de dernière vérification',
                'widget' => 'single_text',
                'attr' => [
                    'max' => (new \DateTime())->format('Y-m-d'),
                    'class' => 'form-control-lg',
                ],
                'help' => 'La date doit être aujourd\'hui ou dans le passé',
            ]);
            
        // Ajouter le champ salle seulement si hide_salle n'est pas à true
        if (!isset($options['hide_salle']) || $options['hide_salle'] === false) {
            $builder->add('salle', EntityType::class, [
                'class' => SalleDeSport::class,
                'choice_label' => 'nom', // Afficher le nom au lieu de l'ID
                'placeholder' => 'Choisir une salle de sport',
                'required' => true,
                'attr' => [
                    'class' => 'select2-control form-control-lg', // Pour permettre une sélection améliorée avec JS
                ],
                'help' => 'Sélectionnez la salle où se trouve cet équipement',
                'query_builder' => function ($er) {
                    return $er->createQueryBuilder('s')
                        ->orderBy('s.nom', 'ASC');
                },
            ]);
        }
        
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
            
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Equipement::class,
            'hide_salle' => false,
            'hide_user' => false,
        ]);
    }
}
