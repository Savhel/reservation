package org.polytech.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("ENSEIGNANT")
public class Enseignant extends Personne {

    @NonNull
    @Column(nullable = false)
    private String tel;
    
    @Column(nullable = false)
    private String grade;

    @OneToMany(mappedBy = "enseignant")
    private List<Cours> cours;

    @OneToMany(mappedBy = "enseignantResponsable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formation> formation;
    
    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSalle> reservationSalles = new ArrayList<>();
    
    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationMateriel> reservationMateriels = new ArrayList<>();
    
    // Constructeur avec tous les attributs hérités de Personne
    public Enseignant(String matricule, String motDePasse, String nom, String prenom, 
                     LocalDate dateNaiss, String lieuNaiss, String mail,
                     String tel, String grade) {
        super(matricule, motDePasse, nom, prenom, dateNaiss, lieuNaiss, mail);
        this.tel = tel;
        this.grade = grade;
    }
    
    // Constructeur avec attributs requis
    public Enseignant(String nom, String prenom, String motDePasse, LocalDate dateNaiss, 
                     String lieuNaiss, String tel, String grade) {
        super(null, motDePasse, nom, prenom, dateNaiss, lieuNaiss, null);
        this.tel = tel;
        this.grade = grade;
    }
    
    // Méthode pour réserver une salle
    public ReservationSalle reserverSalle(Salle salle, Cours cours, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        ReservationSalle reservation = new ReservationSalle();
        reservation.setType("Cours");
        reservation.setSalle(salle);
        reservation.setCours(cours);
        reservation.setEnseignant(this);
        reservation.setDateReservation(date);
        reservation.setHeureDebut(heureDebut);
        reservation.setHeureFin(heureFin);
        return reservation;
    }
    
    // Méthode pour voir le récapitulatif horaire
    public List<ReservationSalle> voirRecapitulatifHoraire() {
        return this.reservationSalles;
    }
    
    // Méthode pour vérifier si l'enseignant est responsable d'une formation
    public boolean estResponsable() {
        return this.formation != null && !this.formation.isEmpty();
    }

}
