package org.polytech.reservation.service.cours.create;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCours implements Creatable<Cours> {

    @Autowired
    private CoursRepository coursRepository;

    @Override
    public Cours create(Cours cours) {
        return coursRepository.save(cours);
    }
}
