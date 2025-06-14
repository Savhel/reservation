package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.models.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, UUID> {

    List<Cours> getCoursByEnseignant(Enseignant enseignant);
    List<Cours> getCoursBySalle(Salle salle);

    List<Cours> getCoursByFormation(Formation formation);
}
