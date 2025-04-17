<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\TelType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Regex;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $isEdit = $options['is_edit'] ?? false;
        $isFrontEdit = $options['front_edit'] ?? false;
        
        $builder
            ->add('firstname', TextType::class, [
                'label' => 'First Name',
                'attr' => ['class' => 'form-control', 'placeholder' => 'First Name'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter a first name']),
                    new Length(['min' => 2, 'max' => 50])
                ]
            ])
            ->add('lastname', TextType::class, [
                'label' => 'Last Name',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Last Name'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter a last name']),
                    new Length(['min' => 2, 'max' => 50])
                ]
            ])
            ->add('email', EmailType::class, [
                'label' => 'Email',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Email Address'],
                'constraints' => [
                    new NotBlank(['message' => 'Please enter an email']),
                    new Email(['message' => 'Please enter a valid email address'])
                ]
            ])
            ->add('password_hash', PasswordType::class, [
                'label' => 'Password',
                'attr' => ['class' => 'form-control', 'placeholder' => '••••••••'],
                'required' => !$isEdit,
                'constraints' => $isEdit ? [] : [
                    new NotBlank(['message' => 'Please enter a password']),
                    new Length(['min' => 6, 'minMessage' => 'Your password should be at least {{ limit }} characters'])
                ],
                'empty_data' => ''
            ])
            ->add('phonenumber', TelType::class, [
                'label' => 'Phone Number',
                'attr' => ['class' => 'form-control', 'placeholder' => '+216 XX XXX XXX'],
                'required' => false,
                'constraints' => [
                    new Regex([
                        'pattern' => '/^\+?[0-9]{8,14}$/',
                        'message' => 'Please enter a valid phone number'
                    ])
                ]
            ]);
            
        // N'ajouter ces champs que si nous ne sommes pas en mode édition frontend
        if (!$isFrontEdit) {
            $builder
                ->add('role', ChoiceType::class, [
                    'label' => 'Role',
                    'choices' => [
                        'User' => 'ROLE_USER',
                        'Admin' => 'ROLE_ADMIN',
                        'Coach' => 'ROLE_COACH',
                    ],
                    'attr' => ['class' => 'form-select']
                ])
                ->add('subscription_end_date', DateType::class, [
                    'label' => 'Subscription End Date',
                    'widget' => 'single_text',
                    'attr' => ['class' => 'form-control'],
                    'data' => new \DateTime('+1 month')
                ])
                ->add('is_active', CheckboxType::class, [
                    'label' => 'Active Account',
                    'required' => false,
                    'attr' => ['class' => 'form-check-input'],
                    'data' => true
                ])
                ->add('violation_count', IntegerType::class, [
                    'label' => 'Violation Count',
                    'attr' => ['class' => 'form-control', 'min' => 0],
                    'required' => false,
                    'data' => 0
                ])
                ->add('location', TextType::class, [
                    'label' => 'Location',
                    'attr' => ['class' => 'form-control', 'placeholder' => 'Location'],
                    'required' => false
                ])
                ->add('cin', TextType::class, [
                    'label' => 'CIN',
                    'attr' => ['class' => 'form-control', 'placeholder' => 'Identity Card Number'],
                    'required' => false,
                    'constraints' => [
                        new Regex([
                            'pattern' => '/^[0-9]{8}$/',
                            'message' => 'CIN must be 8 digits'
                        ])
                    ]
                ])
                ->add('age', IntegerType::class, [
                    'label' => 'Age',
                    'attr' => ['class' => 'form-control', 'min' => 16, 'max' => 120],
                    'required' => false
                ]);
        }
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
            'is_edit' => false,
            'front_edit' => false,
        ]);
    }
}
