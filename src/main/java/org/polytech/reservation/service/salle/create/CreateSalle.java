package org.polytech.reservation.service.salle.create;

import org.polytech.reservation.models.Salle;
import org.polytech.reservation.repositories.SalleRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSalle implements Creatable<Salle> {

    @Autowired
    private SalleRepository salleRepository;

    @Override
    public Salle create(Salle salle) {
        return salleRepository.save(salle);
    }
}
