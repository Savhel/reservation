package org.polytech.reservation.service.enseignant.create;

import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateEnseignant implements Creatable<Enseignant> {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Override
    public Enseignant create(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }
}
