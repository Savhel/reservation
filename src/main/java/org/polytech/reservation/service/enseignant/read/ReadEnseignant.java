package org.polytech.reservation.service.enseignant.read;

import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.models.Enseignant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadEnseignant {

    @Autowired
    private EnseignantRepository EnseignantRepository;

    public Enseignant read(String matricule){
        return EnseignantRepository.findById(matricule).orElse(null);
    }
}
