package org.polytech.reservation.service.cours.read;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.repositories.SalleRepository;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.interfaces.Readable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadCours implements Readable<Cours>{

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Override
    public Cours read(UUID idCours) {
        return coursRepository.findById(idCours).orElse(null);
    }
    @Override
    public List<Cours> readlist() {
        return coursRepository.findAll();
    }

    public List<Cours> listCoursEnseignant(String matricule) {
        if (enseignantRepository.findById(matricule).isPresent()){
            return coursRepository.getCoursByEnseignant(enseignantRepository.findById(matricule).get());
        }
        throw new IDNotExist("Nous ne connaissons pas cet utilisateur");
    }

    public List<Cours> listCoursSalle(UUID numSalle) {
        if (salleRepository.findById(numSalle).isPresent()){
            return coursRepository.getCoursBySalle(salleRepository.findById(numSalle).get());
        }
        throw new IDNotExist ("Nous ne connaissons pas cette salle");
    }

    public List<Cours> listCoursFormation(UUID idFormation) {
        if (formationRepository.findById(idFormation).isPresent()){
            return coursRepository.getCoursByFormation(formationRepository.findById(idFormation).get());
        }
        throw new IDNotExist ("Nous ne connaissons pas cette formation");
    }
}
