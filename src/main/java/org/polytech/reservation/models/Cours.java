package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idCours;

    @NonNull
    @Column(nullable = false)
    private String sujet;

    @NonNull
    @Column(nullable = false)
    private int nombreHeures;

    @NonNull
    @Column(nullable = false)
    private LocalDate jour;

    @NonNull
    @Column(nullable = false)
    private Time heure;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "matriculeEnseignant", nullable = false)
    private Enseignant enseignant;

    @ManyToOne
    @JoinColumn(name = "idFormation")
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "numSalle")
    private Salle salle;
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationMateriel> reservationMateriels = new ArrayList<>();
    
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSalle> reservationSalles = new ArrayList<>();
    
    // Méthode pour ajouter un cours
    public static Cours ajouterCours(String sujet, int nombreHeures, LocalDate jour, Time heure, Enseignant enseignant) {
        return new Cours(sujet, nombreHeures, jour, heure, enseignant);
    }
    
    // Méthode pour modifier un cours
    public Cours modifierCours(String sujet, int nombreHeures, LocalDate jour, Time heure) {
        this.sujet = sujet;
        this.nombreHeures = nombreHeures;
        this.jour = jour;
        this.heure = heure;
        return this;
    }
    
    // Méthode pour supprimer un cours (à utiliser dans le service)
    public boolean supprimerCours() {
        // Vérifier si le cours n'a pas de réservations en cours
        return (this.reservationMateriels == null || this.reservationMateriels.isEmpty()) &&
               (this.reservationSalles == null || this.reservationSalles.isEmpty());
    }



}
