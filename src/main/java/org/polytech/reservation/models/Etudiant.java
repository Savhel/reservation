package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ETUDIANT")
public class Etudiant extends Personne {

    @Column(nullable = false)
    private String niveau;

    @Column(nullable = false)
    private String filiere;

    // Constructeur avec tous les attributs hérités de Personne
    public Etudiant(String matricule, String motDePasse, String nom, String prenom, 
                   LocalDate dateNaiss, String lieuNaiss, String mail,
                   String niveau, String filiere) {
        super(matricule, motDePasse, nom, prenom, dateNaiss, lieuNaiss, mail);
        this.niveau = niveau;
        this.filiere = filiere;
    }

    // Constructeur avec attributs requis de Personne et attributs spécifiques d'Etudiant
    public Etudiant(String nom, String prenom, String motDePasse, LocalDate dateNaiss, 
                   String lieuNaiss, String niveau, String filiere) {
        super(null, motDePasse, nom, prenom, dateNaiss, lieuNaiss, null);
        this.niveau = niveau;
        this.filiere = filiere;
    }
}