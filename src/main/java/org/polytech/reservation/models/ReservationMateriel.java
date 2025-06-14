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
public class ReservationMateriel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idReservation;
    
    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "idMateriel")
    private MaterielPedagogique materiel;

    @ManyToOne
    @JoinColumn(name = "idCours")
    private Cours cours;
    
    @ManyToOne
    @JoinColumn(name = "matricule")
    private Enseignant enseignant;
    
    @Column(nullable = false)
    private LocalDate dateReservation;
    
    @Column(nullable = false)
    private LocalTime heureDebut;
    
    @Column(nullable = false)
    private LocalTime heureFin;
    
    @Column(nullable = false)
    private boolean confirmee = false;
}
