package org.polytech.reservation.service.salle.delete;

import org.polytech.reservation.models.Salle;
import org.polytech.reservation.repositories.SalleRepository;
import org.polytech.reservation.service.interfaces.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteSalle implements Deletable<Salle> {

    @Autowired
    private SalleRepository salleRepository;


    @Override
    public boolean delete(UUID idSalle) {
        salleRepository.deleteById(idSalle);
        return false;
    }
}
