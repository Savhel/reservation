package org.polytech.reservation.service.cours.update;

import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.service.interfaces.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateCours implements Updatable<Cours>{

    @Autowired
    private CoursRepository coursRepository;

    @Override
    public Cours update(Cours cours, UUID idCours) {
        Optional<Cours> oldCours = coursRepository.findById(idCours);

        if(oldCours.isPresent()){
            Cours newCours = oldCours.get();
            newCours.setSujet(cours.getSujet());
            newCours.setNombreHeures(cours.getNombreHeures());
            newCours.setJour(cours.getJour());
            newCours.setHeure(cours.getHeure());
            newCours.setEnseignant(cours.getEnseignant());
            newCours.setSalle(cours.getSalle());
            newCours.setFormation(cours.getFormation());
            newCours.setReservationMateriels(cours.getReservationMateriels());
            return coursRepository.save(newCours);

        }
        throw new ModificationImpossible("Une erreur est survenue lors de la modification de vos informations");
    }

}
