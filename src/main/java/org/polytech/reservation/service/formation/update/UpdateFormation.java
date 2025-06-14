package org.polytech.reservation.service.formation.update;

import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.interfaces.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateFormation implements Updatable<Formation> {

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public Formation update(Formation formation, UUID idFormation) {
        Optional<Formation> oldFormation = formationRepository.findById(idFormation);
        if (oldFormation.isPresent()){
            Formation newFormation = oldFormation.get();
            newFormation.setIntitule(formation.getIntitule());
            newFormation.setNbPlaces(formation.getNbPlaces());
            newFormation.setEnseignantResponsable(formation.getEnseignantResponsable());
            newFormation.setCours(formation.getCours());
            return formationRepository.save(newFormation);
        }
        throw new ModificationImpossible("Une erreur est survenue lors de la modification de vos informations");
    }
}
