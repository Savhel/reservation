package org.polytech.reservation.service.DTO;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Salle;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.service.DTO.classes.EnseignantDTO;
import org.polytech.reservation.service.DTO.classes.SalleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DTOMapper {

    public static SalleDTO toSalleDTO(Salle salle) {
        return new SalleDTO(salle.getNumSalle(), salle.getNomSalle(), salle.getNbPlace());
    }

    public static EnseignantDTO toEnseignantDTO(Enseignant enseignant) {
        return new EnseignantDTO(enseignant.getMatricule(),enseignant.getMotDePasse(), enseignant.getNom(), enseignant.getPrenom(), enseignant.getMail(), enseignant.getTel());
    }
    public static CoursDTO toCoursDTO(Cours cours) {
        return new CoursDTO(cours.getIdCours(), cours.getSujet(), cours.getNombreHeures(), cours.getJour(), cours.getHeure(), toEnseignantDTO(cours.getEnseignant()), cours.getSalle() != null ? toSalleDTO(cours.getSalle()) : null);
    }
    public static List<CoursDTO> toCoursDTOList(List<Cours> coursList) {
        return coursList.stream().map(cours -> toCoursDTO(cours)).collect(Collectors.toList());
    }
}
