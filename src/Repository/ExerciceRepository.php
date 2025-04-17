<?php

namespace App\Repository;

use App\Entity\Exercice;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Exercice>
 */
class ExerciceRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Exercice::class);
    }

    /**
     * Find exercises by various filters
     *
     * @param string|null $search Search term for exercise name
     * @param string|null $muscle Specific muscle group to filter by
     * @param string $sort Sorting option ('name_asc', 'name_desc', 'date_asc', 'date_desc')
     * @return Exercice[] Returns an array of Exercice objects
     */
    public function findByFilters(?string $search = null, ?string $muscle = null, string $sort = 'name_asc'): array
    {
        $qb = $this->createQueryBuilder('e');
        
        // Filter by name
        if ($search) {
            $qb->andWhere('e.nom_exercice LIKE :search')
               ->setParameter('search', '%' . $search . '%');
        }
        
        // Filter by muscle group
        if ($muscle) {
            $qb->andWhere('e.muscle = :muscle')
               ->setParameter('muscle', $muscle);
        }
        
        // Sort results
        switch ($sort) {
            case 'name_desc':
                $qb->orderBy('e.nom_exercice', 'DESC');
                break;
            case 'date_asc':
                $qb->orderBy('e.id', 'ASC');
                break;
            case 'date_desc':
                $qb->orderBy('e.id', 'DESC');
                break;
            case 'name_asc':
            default:
                $qb->orderBy('e.nom_exercice', 'ASC');
                break;
        }
        
        return $qb->getQuery()->getResult();
    }

//    /**
//     * @return Exercice[] Returns an array of Exercice objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('e.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Exercice
//    {
//        return $this->createQueryBuilder('e')
//            ->andWhere('e.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
