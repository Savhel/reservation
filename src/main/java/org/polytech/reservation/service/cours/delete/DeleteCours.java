package org.polytech.reservation.service.cours.delete;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.service.interfaces.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCours implements Deletable<Cours> {

    @Autowired
    private CoursRepository coursRepository;

    @Override
    public boolean delete(UUID idCours) {
        coursRepository.deleteById(idCours);
        return true;
    }


}
