package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.formation.create.CreateFormation;
import org.polytech.reservation.service.formation.delete.DeleteFormation;
import org.polytech.reservation.service.formation.read.readFormation;
import org.polytech.reservation.service.formation.update.UpdateFormation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormationService {

    private final FormationRepository formationRepository;
    private final EnseignantRepository enseignantRepository;
    private final CreateFormation createFormation;
    private final readFormation readFormation;
    private final UpdateFormation updateFormation;
    private final DeleteFormation deleteFormation;

    @Autowired
    public FormationService(FormationRepository formationRepository, 
                           EnseignantRepository enseignantRepository,
                           CreateFormation createFormation, 
                           readFormation readFormation,
                           UpdateFormation updateFormation, 
                           DeleteFormation deleteFormation) {
        this.formationRepository = formationRepository;
        this.enseignantRepository = enseignantRepository;
        this.createFormation = createFormation;
        this.readFormation = readFormation;
        this.updateFormation = updateFormation;
        this.deleteFormation = deleteFormation;
    }

    // Méthode pour créer une formation
    public Formation creerFormation(String theme, int nombreDePlaces, String matriculeEnseignant) {
        Optional<Enseignant> enseignant = enseignantRepository.findById(matriculeEnseignant);
        if (enseignant.isEmpty()) {
            throw new IDNotExist("L'enseignant avec le matricule " + matriculeEnseignant + " n'existe pas");
        }
        
        Formation formation = Formation.ajouterFormation(theme, nombreDePlaces, enseignant.get());
        return createFormation.create(formation);
    }

    // Méthode pour récupérer toutes les formations
    public List<Formation> getAllFormations() {
        return readFormation.readlist();
    }

    // Méthode pour récupérer une formation par son ID
    public Formation getFormationById(UUID idFormation) {
        Formation formation = readFormation.read(idFormation);
        if (formation == null) {
            throw new IDNotExist("La formation avec l'ID " + idFormation + " n'existe pas");
        }
        return formation;
    }

    // Méthode pour récupérer les formations d'un enseignant responsable
    public List<Formation> getFormationsByEnseignant(String matricule) {
        return readFormation.listFormationEnseignant(matricule);
    }

    // Méthode pour modifier une formation
    public Formation modifierFormation(UUID idFormation, String theme, int nombreDePlaces, String matriculeEnseignant) {
        Formation formation = getFormationById(idFormation);
        
        Optional<Enseignant> enseignant = enseignantRepository.findById(matriculeEnseignant);
        if (enseignant.isEmpty()) {
            throw new IDNotExist("L'enseignant avec le matricule " + matriculeEnseignant + " n'existe pas");
        }
        
        formation.setTheme(theme);
        formation.setNombreDePlaces(nombreDePlaces);
        formation.setEnseignantResponsable(enseignant.get());
        
        return formationRepository.save(formation);
    }

    // Méthode pour supprimer une formation
    public boolean supprimerFormation(UUID idFormation) {
        Formation formation = getFormationById(idFormation);
        
        // Vérifier si la formation peut être supprimée
        if (!formation.supprimerFormation()) {
            throw new SuppressionImpossible("Impossible de supprimer la formation car elle a des cours associés");
        }
        
        return deleteFormation.delete(idFormation);
    }
}
