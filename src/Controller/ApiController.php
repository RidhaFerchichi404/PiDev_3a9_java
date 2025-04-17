<?php

namespace App\Controller;

use App\Service\RegionService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Attribute\Route;

#[Route('/api')]
class ApiController extends AbstractController
{
    #[Route('/regions', name: 'api_regions', methods: ['GET'])]
    public function getRegions(RegionService $regionService): JsonResponse
    {
        $regionsData = $regionService->getAllRegionsWithZones();
        
        return $this->json($regionsData);
    }
    
    #[Route('/regions/{region}/zones', name: 'api_zones_by_region', methods: ['GET'])]
    public function getZonesByRegion(string $region, RegionService $regionService): JsonResponse
    {
        $zones = $regionService->getZonesByRegion($region);
        
        if (empty($zones)) {
            return $this->json(['error' => 'Region not found'], 404);
        }
        
        return $this->json($zones);
    }
} 