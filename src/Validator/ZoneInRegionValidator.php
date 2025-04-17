<?php

namespace App\Validator;

use App\Service\RegionService;
use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;

class ZoneInRegionValidator extends ConstraintValidator
{
    private RegionService $regionService;

    public function __construct(RegionService $regionService)
    {
        $this->regionService = $regionService;
    }

    public function validate($value, Constraint $constraint)
    {
        if (!$constraint instanceof ZoneInRegion) {
            throw new UnexpectedTypeException($constraint, ZoneInRegion::class);
        }

        if (null === $value || '' === $value) {
            return;
        }

        $object = $this->context->getObject();
        $region = $object->getRegion();

        if (!$region) {
            return;
        }

        if ($value === "Choisissez d'abord une région" || 
            strpos($value, 'Choisissez une ville dans') === 0 ||
            $value === 'Aucune ville disponible pour cette région') {
            return;
        }

        // Vérifier si la zone appartient à la région
        if (!$this->regionService->isZoneInRegion($value, $region)) {
            $this->context->buildViolation($constraint->message)
                ->setParameter('{{ zone }}', $value)
                ->setParameter('{{ region }}', $region)
                ->addViolation();
        }
    }
} 