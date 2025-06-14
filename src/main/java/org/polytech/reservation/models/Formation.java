package org.polytech.reservation.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idFormation;

    @NonNull
    @Column(nullable = false)
    private String theme;

    @NonNull
    @Column(nullable = false)
    private int nombreDePlaces;

    @ManyToOne
    @JoinColumn(name = "matriculeResponsable")
    private Enseignant enseignantResponsable;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cours> cours = new ArrayList<>();
    
    // Méthode pour ajouter une formation
    public static Formation ajouterFormation(String theme, int nombreDePlaces, Enseignant enseignantResponsable) {
        Formation formation = new Formation(theme, nombreDePlaces);
        formation.setEnseignantResponsable(enseignantResponsable);
        return formation;
    }
    
    // Méthode pour modifier une formation
    public Formation modifierFormation(String theme, int nombreDePlaces, Enseignant enseignantResponsable) {
        this.theme = theme;
        this.nombreDePlaces = nombreDePlaces;
        this.enseignantResponsable = enseignantResponsable;
        return this;
    }
    
    // Méthode pour supprimer une formation (à utiliser dans le service)
    public boolean supprimerFormation() {
        // Vérifier si la formation n'a pas de cours associés
        return this.cours == null || this.cours.isEmpty();
    }
    
    // Méthode pour ajouter un cours à la formation
    public Formation ajouterCours(Cours cours) {
        this.cours.add(cours);
        cours.setFormation(this);
        return this;
    }
    
    // Méthode pour lister les formations (à implémenter dans le service)
    public static List<Formation> listerFormations() {
        // Cette méthode sera implémentée dans le service
        return null;
    }
}
