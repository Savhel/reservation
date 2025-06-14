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
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID numSalle;

    @NonNull
    @Column(nullable = false)
    private String nomSalle;

    @NonNull
    @Column(nullable = false)
    private String localisation;

    @NonNull
    @Column(nullable = false)
    private int capaciteMax;

    @OneToMany(mappedBy = "salle")
    private List<Cours> cours;
    
    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSalle> reservations;
    
    // Méthode pour ajouter une salle
    public static Salle ajouterSalle(String nomSalle, String localisation, int capaciteMax) {
        return new Salle(nomSalle, localisation, capaciteMax);
    }
    
    // Méthode pour modifier une salle
    public Salle modifierSalle(String nomSalle, String localisation, int capaciteMax) {
        this.nomSalle = nomSalle;
        this.localisation = localisation;
        this.capaciteMax = capaciteMax;
        return this;
    }
    
    // Méthode pour supprimer une salle (à utiliser dans le service)
    public boolean supprimerSalle() {
        // Vérifier si la salle n'a pas de réservations en cours
        return this.reservations == null || this.reservations.isEmpty();
    }
    
    // Méthode pour lister les salles (à implémenter dans le service)
    public static List<Salle> listerSalles() {

        return null;
    }
}
