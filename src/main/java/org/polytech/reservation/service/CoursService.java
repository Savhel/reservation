package org.polytech.reservation.service;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.service.DTO.DTOMapper;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.service.cours.create.CreateCours;
import org.polytech.reservation.service.cours.delete.DeleteCours;
import org.polytech.reservation.service.cours.read.ReadCours;
import org.polytech.reservation.service.cours.update.UpdateCours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CoursService {

    @Autowired
    private CreateCours createCours;

    @Autowired
    private DeleteCours deleteCours;

    @Autowired
    private UpdateCours updateCours;

    @Autowired
    private ReadCours readCours;

    public CoursDTO createCours(Cours cours) {
        return DTOMapper.toCoursDTO(createCours.create(cours));
    }
    public CoursDTO updateCours(Cours cours, UUID idCours) {
        return DTOMapper.toCoursDTO(updateCours.update(cours, idCours));
    }
    public boolean deleteCours(UUID idCours) {
        return deleteCours.delete(idCours);
    }
    public CoursDTO readCours(UUID idCours) {
        return DTOMapper.toCoursDTO(readCours.read(idCours));
    }
    public List<CoursDTO> readAllCours() {
        return DTOMapper.toCoursDTOList(readCours.readlist());
    }
    public List<CoursDTO> readAllCoursByEnseignant(String matricule) {
        return DTOMapper.toCoursDTOList(readCours.listCoursEnseignant(matricule));
    }
    public List<CoursDTO> readAllCoursByFormation(UUID idFormation) {
        return DTOMapper.toCoursDTOList(readCours.listCoursFormation(idFormation));
    }
    public List<CoursDTO> readAllCoursBySalle(UUID numSalle) {
        return DTOMapper.toCoursDTOList(readCours.listCoursSalle(numSalle));
    }

    public Cours getCoursById(UUID idCours){
        return readCours.read(idCours);
    }

}
