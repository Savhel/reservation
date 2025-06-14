package org.polytech.reservation.service.DTO.classes;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.models.Salle;

import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class CoursDTO {

    @NonNull
    private UUID idCours;

    @NonNull
    private String titre; // Correspond à sujet dans le modèle Cours

    @NonNull
    private int nbHeure; // Correspond à nombreHeures dans le modèle Cours

    @NonNull
    private LocalDate jour;

    @NonNull
    private Time heure;

    @NonNull
    private EnseignantDTO enseignant;

    private SalleDTO salle;
}
