package org.polytech.reservation.service.salle.read;

import org.polytech.reservation.models.Salle;
import org.polytech.reservation.service.interfaces.Readable;
import org.polytech.reservation.repositories.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadSalle implements Readable<Salle> {

    @Autowired
    private SalleRepository salleRepository;

    @Override
    public Salle read(UUID idSalle) {
        return salleRepository.findById(idSalle).orElse(null);
    }

    @Override
    public List<Salle> readlist() {
        return salleRepository.findAll();
    }
}
