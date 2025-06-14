package org.polytech.reservation.service.formation.delete;

import org.polytech.reservation.models.Formation;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.interfaces.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteFormation implements Deletable<Formation> {

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public boolean delete(UUID idFormation) {
        formationRepository.deleteById(idFormation);
        return true;
    }
}
