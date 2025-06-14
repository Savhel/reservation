package org.polytech.reservation.service.formation.create;

import org.polytech.reservation.models.Formation;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateFormation implements Creatable<Formation> {

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public Formation create(Formation formation) {
        return formationRepository.save(formation);
    }
}
