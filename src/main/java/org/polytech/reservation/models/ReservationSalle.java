package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReservationSalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idReservation;

    @NonNull
    @Column(nullable = false)
    private String type; // Type de réservation (cours, réunion, etc.)

    @NonNull
    @ManyToOne
    @JoinColumn(name = "numSalle")
    private Salle salle;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "idCours")
    private Cours cours;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "matriculeEnseignant")
    private Enseignant enseignant;

    @NonNull
    @Column(nullable = false)
    private LocalDate dateReservation;

    @NonNull
    @Column(nullable = false)
    private LocalTime heureDebut;

    @NonNull
    @Column(nullable = false)
    private LocalTime heureFin;

}