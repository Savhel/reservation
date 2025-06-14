package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SalleRepository extends JpaRepository<Salle, UUID> {
    // Rechercher des salles par nom
    List<Salle> findByNomSalleContainingIgnoreCase(String nomSalle);
    
    // Rechercher des salles par localisation
    List<Salle> findByLocalisationContainingIgnoreCase(String localisation);
    
    // Rechercher des salles par capacité minimale
    List<Salle> findByCapaciteMaxGreaterThanEqual(int capaciteMin);
    
    // Vérifier si une salle existe par son nom
    boolean existsByNomSalleIgnoreCase(String nomSalle);
}
