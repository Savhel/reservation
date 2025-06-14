package org.polytech.reservation.service.formation.read;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.repositories.FormationRepository;
import org.polytech.reservation.service.interfaces.Readable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class readFormation implements Readable<Formation> {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Override
    public Formation read(UUID idFormation) {
        return formationRepository.findById(idFormation).orElse(null);
    }

    @Override
    public List<Formation> readlist() {
        return formationRepository.findAll();
    }

    public List<Formation> listFormationEnseignant(String matricule) {
        if (enseignantRepository.findById(matricule).isPresent()){
            return formationRepository.getFormationByEnseignantResponsable(enseignantRepository.findById(matricule).get());
        }
        throw new IDNotExist("Nous ne connaissons pas cet utilisateur");
    }
}
