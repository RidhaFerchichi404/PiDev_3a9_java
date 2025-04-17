<?php

namespace App\Validator;

use Symfony\Component\Validator\Constraint;

/**
 * @Annotation
 */
#[\Attribute]
class ZoneInRegion extends Constraint
{
    public string $message = 'La ville "{{ zone }}" n\'appartient pas à la région "{{ region }}".';

    public function validatedBy()
    {
        return ZoneInRegionValidator::class;
    }
} 