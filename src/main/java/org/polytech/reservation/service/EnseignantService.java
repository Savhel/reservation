package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;

    @Autowired
    public EnseignantService(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    // Créer un enseignant
    public Enseignant creerEnseignant(String nom, String prenom, String motDePasse, 
                                     LocalDate dateNaiss, String lieuNaiss, String mail,
                                     String tel, String grade) {
        Enseignant enseignant = new Enseignant();
        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setMotDePasse(motDePasse);
        enseignant.setDateNaiss(dateNaiss);
        enseignant.setLieuNaiss(lieuNaiss);
        enseignant.setMail(mail);
        enseignant.setTel(tel);
        enseignant.setGrade(grade);
        return enseignantRepository.save(enseignant);
    }

    // Obtenir tous les enseignants
    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    // Obtenir un enseignant par son matricule
    public Enseignant getEnseignantByMatricule(String matricule) throws IDNotExist {
        return enseignantRepository.findById(matricule)
                .orElseThrow(() -> new IDNotExist("Enseignant non trouvé"));
    }

    // Obtenir des enseignants par nom
    public List<Enseignant> getEnseignantsByNom(String nom) {
        return enseignantRepository.findByNomContainingIgnoreCase(nom);
    }

    // Obtenir des enseignants par prénom
    public List<Enseignant> getEnseignantsByPrenom(String prenom) {
        return enseignantRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    // Obtenir des enseignants par grade
    public List<Enseignant> getEnseignantsByGrade(String grade) {
        return enseignantRepository.findByGradeContainingIgnoreCase(grade);
    }

    // Obtenir des enseignants par nom et prénom
    public List<Enseignant> getEnseignantsByNomAndPrenom(String nom, String prenom) {
        return enseignantRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(nom, prenom);
    }

    // Modifier un enseignant
    public Enseignant modifierEnseignant(String matricule, String nom, String prenom, String motDePasse, 
                                        LocalDate dateNaiss, String lieuNaiss, String mail,
                                        String tel, String grade) throws IDNotExist, ModificationImpossible {
        Enseignant enseignant = enseignantRepository.findById(matricule)
                .orElseThrow(() -> new IDNotExist("Enseignant non trouvé"));

        enseignant.setNom(nom);
        enseignant.setPrenom(prenom);
        enseignant.setMotDePasse(motDePasse);
        enseignant.setDateNaiss(dateNaiss);
        enseignant.setLieuNaiss(lieuNaiss);
        enseignant.setMail(mail);
        enseignant.setTel(tel);
        enseignant.setGrade(grade);

        return enseignantRepository.save(enseignant);
    }

    // Supprimer un enseignant
    public void supprimerEnseignant(String matricule) throws IDNotExist, SuppressionImpossible {
        Enseignant enseignant = enseignantRepository.findById(matricule)
                .orElseThrow(() -> new IDNotExist("Enseignant non trouvé"));

        // Vérifier si l'enseignant peut être supprimé (par exemple, s'il n'a pas de cours ou de formations associés)
        if (!peutEtreSupprime(enseignant)) {
            throw new SuppressionImpossible("L'enseignant ne peut pas être supprimé car il a des cours ou des formations associés");
        }

        enseignantRepository.deleteById(matricule);
    }

    // Vérifier si un enseignant peut être supprimé
    private boolean peutEtreSupprime(Enseignant enseignant) {
        // Un enseignant peut être supprimé s'il n'a pas de cours ou de formations associés
        return (enseignant.getCours() == null || enseignant.getCours().isEmpty()) &&
               (enseignant.getFormation() == null || enseignant.getFormation().isEmpty());
    }
}
