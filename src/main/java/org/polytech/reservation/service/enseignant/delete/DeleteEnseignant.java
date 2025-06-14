package org.polytech.reservation.service.enseignant.delete;

import org.polytech.reservation.repositories.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DeleteEnseignant{

    @Autowired
    private EnseignantRepository enseignantRepository;


    public Boolean delete(String matricule) {
        enseignantRepository.deleteById(matricule);
        return true;
    }
}
