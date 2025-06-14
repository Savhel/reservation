package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class VideoProjecteur {

    @Id
    private UUID idProjecteur;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idProjecteur")
    private MaterielPedagogique materiel;


    @Column(nullable = false)
    private String definition;
}
