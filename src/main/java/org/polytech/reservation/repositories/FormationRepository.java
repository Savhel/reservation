package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FormationRepository extends JpaRepository<Formation, UUID> {

    List<Formation> getFormationByEnseignantResponsable(Enseignant enseignant);
}
