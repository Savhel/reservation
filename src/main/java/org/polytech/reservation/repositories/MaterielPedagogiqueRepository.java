package org.polytech.reservation.repositories;

import org.polytech.reservation.models.MaterielPedagogique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterielPedagogiqueRepository extends JpaRepository<MaterielPedagogique, UUID> {
    
    // Trouver un matériel par son nom
    Optional<MaterielPedagogique> findByNom(String nom);
    
    // Trouver des matériels par marque
    List<MaterielPedagogique> findByMarque(String marque);
    
    // Vérifier si un matériel existe par son nom
    boolean existsByNom(String nom);
}
