package org.polytech.reservation.service.enseignant.update;

import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateEnseignant {
    
    @Autowired
    private EnseignantRepository enseignantRepository;

    public Enseignant update(Enseignant enseignant, String matricule){

        Optional<Enseignant> oldEnseignant = enseignantRepository.findById(matricule);

        if(oldEnseignant.isPresent()){
            Enseignant newEnseignant = oldEnseignant.get();
            newEnseignant.setNom(enseignant.getNom());
            newEnseignant.setPrenom(enseignant.getPrenom());
            newEnseignant.setMail(enseignant.getMail());
            newEnseignant.setTel(enseignant.getTel());
            newEnseignant.setMotDePasse(enseignant.getMotDePasse());
            newEnseignant.setCours(enseignant.getCours());
            newEnseignant.setFormation(enseignant.getFormation());

            return enseignantRepository.save(newEnseignant);

        }
        throw new ModificationImpossible("Une erreur est survenue lors de la modification de vos informations");
    }
}
