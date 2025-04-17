<?php

namespace App\Service;

class RegionService
{
    /**
     * Tableau associatif complet des régions de Tunisie et leurs villes
     */
    private array $regions = [
        'Ariana' => ['Ariana', 'Raoued', 'Ettadhamen', 'Kalâat el-Andalous', 'Sidi Thabet', 'Soukra', 'Mnihla', 'Borj Louzir', 'Borj El Baccouche', 'Ennasr', 'Cité Ghazela'],
        'Béja' => ['Béja', 'Medjez el-Bab', 'Téboursouk', 'Testour', 'Nefza', 'Amdoun', 'Goubellat', 'Thibar', 'Zahret Mediène'],
        'Ben Arous' => ['Ben Arous', 'El Mourouj', 'Radès', 'Mégrine', 'Hammam Lif', 'Mornag', 'Hammam Chott', 'Boumhel', 'Ezzahra', 'Fouchana', 'Nouvelle Médina', 'Khélidia', 'Borj Cédria'],
        'Bizerte' => ['Bizerte', 'Menzel Bourguiba', 'Mateur', 'Ras Jebel', 'Sejnane', 'Tinja', 'El Alia', 'Ghar El Melh', 'Menzel Jemil', 'Metline', 'Utique', 'Joumine', 'Ghezala'],
        'Gabès' => ['Gabès', 'El Hamma', 'Mareth', 'Matmata', 'Métouia', 'Menzel Habib', 'Nouvelle Matmata', 'Ghannouch'],
        'Gafsa' => ['Gafsa', 'Métlaoui', 'El Ksar', 'Moularès', 'Redeyef', 'Mdhilla', 'El Guettar', 'Sened', 'Belkhir'],
        'Jendouba' => ['Jendouba', 'Bou Salem', 'Tabarka', 'Aïn Draham', 'Ghardimaou', 'Fernana', 'Beni M\'Tir', 'Oued Mliz', 'Balta-Bou Aouane'],
        'Kairouan' => ['Kairouan', 'Sbikha', 'Haffouz', 'Hajeb El Ayoun', 'Chebika', 'Oueslatia', 'Nasrallah', 'Menzel Mehiri', 'Bou Hajla', 'El Alâa'],
        'Kasserine' => ['Kasserine', 'Sbeitla', 'Fériana', 'Thala', 'Foussana', 'Haïdra', 'Jedelienne', 'Sbeïtla', 'Majel Bel Abbès', 'Hassi El Ferid'],
        'Kébili' => ['Kébili', 'Douz', 'Souk Lahad', 'El Golâa', 'Jemna', 'Kébili Nord', 'Faouar'],
        'Le Kef' => ['Le Kef', 'Dahmani', 'Tajerouine', 'Sers', 'Kalâat Khasba', 'Nebeur', 'Sakiet Sidi Youssef', 'Jérissa', 'El Ksour', 'Kalaat Senan'],
        'Mahdia' => ['Mahdia', 'Ksour Essef', 'El Jem', 'Chebba', 'Souassi', 'Ouled Chamekh', 'Hebira', 'Essouassi', 'Bou Merdes', 'Melloulèche'],
        'Manouba' => ['Manouba', 'Djedeida', 'Oued Ellil', 'Tebourba', 'El Battan', 'Mornaguia', 'Borj El Amri', 'Douar Hicher', 'Oued Ellil'],
        'Médenine' => ['Médenine', 'Djerba Houmt Souk', 'Djerba Midoun', 'Djerba Ajim', 'Zarzis', 'Ben Gardane', 'Beni Khedache', 'Sidi Makhlouf'],
        'Monastir' => ['Monastir', 'Moknine', 'Jemmal', 'Ksar Hellal', 'Téboulba', 'Sayada', 'Bembla', 'Sahline', 'Ksibet el-Médiouni', 'Beni Hassen', 'Ouerdanine', 'Zéramdine'],
        'Nabeul' => ['Nabeul', 'Hammamet', 'Kélibia', 'Korba', 'Menzel Temime', 'Grombalia', 'Soliman', 'Menzel Bouzelfa', 'Béni Khalled', 'Béni Khiar', 'El Haouaria', 'Dar Châabane', 'Mida', 'Takelsa'],
        'Sfax' => ['Sfax', 'Sakiet Ezzit', 'Chihia', 'Sakiet Eddaïer', 'Mahres', 'Jebiniana', 'El Hencha', 'Graïba', 'Bir Ali Ben Khalifa', 'Skhira', 'Agareb', 'Thyna', 'Kerkennah'],
        'Sidi Bouzid' => ['Sidi Bouzid', 'Regueb', 'Ouled Haffouz', 'Bir El Hafey', 'Sidi Ali Ben Aoun', 'Jilma', 'Cebbala', 'Menzel Bouzaiane', 'Meknassy', 'Mezzouna', 'Souk Jedid'],
        'Siliana' => ['Siliana', 'Bou Arada', 'Gaâfour', 'El Krib', 'Sidi Bou Rouis', 'Maktar', 'Rohia', 'Kesra', 'Bargou', 'El Aroussa'],
        'Sousse' => ['Sousse', 'Msaken', 'Kalaâ Kebira', 'Akouda', 'Kalaâ Seghira', 'Hammam Sousse', 'Hergla', 'Enfidha', 'Sidi Bou Ali', 'Sidi El Hani', 'Kondar'],
        'Tataouine' => ['Tataouine', 'Ghomrassen', 'Remada', 'Dehiba', 'Bir Lahmar', 'Smar', 'Tataouine Nord', 'Tataouine Sud'],
        'Tozeur' => ['Tozeur', 'Degache', 'Nefta', 'Tamerza', 'Hezoua', 'Hammet Jerid'],
        'Tunis' => ['Tunis', 'La Marsa', 'Le Bardo', 'Le Kram', 'Carthage', 'Sidi Bou Said', 'Ettadhamen-Mnihla', 'La Goulette', 'Mégrine', 'Sidi Hassine', 'El Omrane', 'El Omrane Supérieur', 'El Menzah', 'Cité El Khadra', 'Bab Souika', 'Bab Bhar', 'Séjoumi'],
        'Zaghouan' => ['Zaghouan', 'Zriba', 'Bir Mcherga', 'Djebel Oust', 'El Fahs', 'Nadhour', 'Saouaf'],
    ];

    /**
     * Récupère toutes les régions disponibles
     */
    public function getAllRegions(): array
    {
        return array_keys($this->regions);
    }

    /**
     * Récupère toutes les villes pour une région donnée
     */
    public function getZonesByRegion(string $region): array
    {
        // Recherche directe (sensible à la casse)
        if (isset($this->regions[$region])) {
            return $this->regions[$region];
        }
        
        // Recherche insensible à la casse si la recherche directe échoue
        foreach (array_keys($this->regions) as $key) {
            if (strtolower($key) === strtolower($region)) {
                return $this->regions[$key];
            }
        }
        
        return [];
    }

    /**
     * Vérifie si une ville appartient à une région
     */
    public function isZoneInRegion(string $zone, string $region): bool
    {
        $zones = $this->getZonesByRegion($region);
        
        // Recherche sensible à la casse
        if (in_array($zone, $zones)) {
            return true;
        }
        
        // Recherche insensible à la casse
        foreach ($zones as $z) {
            if (strtolower($z) === strtolower($zone)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Récupère toutes les régions avec leurs villes respectives
     */
    public function getAllRegionsWithZones(): array
    {
        return $this->regions;
    }
} 