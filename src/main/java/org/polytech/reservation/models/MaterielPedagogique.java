package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class MaterielPedagogique {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idMateriel;

    @NonNull
    @Column(nullable = false)
    private String nom;

    @NonNull
    @Column(nullable = false)
    private String marque;

    @OneToMany(mappedBy = "materiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationMateriel> reservationMateriels;
}
