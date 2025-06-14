package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, String> {
    // Rechercher un enseignant par matricule
    boolean existsByMatricule(String matricule);

    Optional<Enseignant> findByMatricule(String matricule);
    
    // Rechercher des enseignants par nom
    List<Enseignant> findByNomContainingIgnoreCase(String nom);
    
    // Rechercher des enseignants par prénom
    List<Enseignant> findByPrenomContainingIgnoreCase(String prenom);
    
    // Rechercher des enseignants par grade
    List<Enseignant> findByGradeContainingIgnoreCase(String grade);
    
    // Rechercher des enseignants par nom et prénom
    List<Enseignant> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(String nom, String prenom);
}
