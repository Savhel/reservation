package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, String> {
    // Rechercher un étudiant par matricule
    boolean existsByMatricule(String matricule);
    
    // Rechercher des étudiants par nom
    List<Etudiant> findByNomContainingIgnoreCase(String nom);
    
    // Rechercher des étudiants par prénom
    List<Etudiant> findByPrenomContainingIgnoreCase(String prenom);
    
    // Rechercher des étudiants par niveau
    List<Etudiant> findByNiveauContainingIgnoreCase(String niveau);
    
    // Rechercher des étudiants par filière
    List<Etudiant> findByFiliereContainingIgnoreCase(String filiere);
    
    // Rechercher des étudiants par nom et prénom
    List<Etudiant> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(String nom, String prenom);
    
    // Rechercher des étudiants par niveau et filière
    List<Etudiant> findByNiveauContainingIgnoreCaseAndFiliereContainingIgnoreCase(String niveau, String filiere);
}