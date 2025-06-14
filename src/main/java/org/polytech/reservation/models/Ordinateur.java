package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Ordinateur {

    @Id
    private UUID idOrdinateur;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idOrdinateur")
    private MaterielPedagogique materiel;

    @Column
    private String disqueDur;

    @Column
    private String RAM;

    @Column
    private String processeur;
}
