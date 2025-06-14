package org.polytech.reservation.service.salle.update;

import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Salle;
import org.polytech.reservation.repositories.SalleRepository;
import org.polytech.reservation.service.interfaces.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateSalle implements Updatable<Salle> {

    @Autowired
    private SalleRepository salleRepository;

    @Override
    public Salle update(Salle salle, UUID idSalle) {
        Optional<Salle> oldSalle = salleRepository.findById(idSalle);
        if (oldSalle.isPresent()) {
            Salle newSalle = oldSalle.get();
            newSalle.setNomSalle(salle.getNomSalle());
            newSalle.setNbPlace(salle.getNbPlace());
            newSalle.setCours(salle.getCours());
            return salleRepository.save(newSalle);
        }
        throw new ModificationImpossible("Une erreur est survenue lors de la modification des informations de la salle");

    }
}
