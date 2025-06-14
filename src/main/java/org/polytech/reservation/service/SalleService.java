package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Salle;
import org.polytech.reservation.repositories.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SalleService {

    private final SalleRepository salleRepository;

    @Autowired
    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    // Créer une salle
    public Salle creerSalle(String nomSalle, String localisation, int capaciteMax) {
        Salle salle = Salle.ajouterSalle(nomSalle, localisation, capaciteMax);
        return salleRepository.save(salle);
    }

    // Obtenir toutes les salles
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    // Obtenir une salle par son ID
    public Salle getSalleById(UUID id) throws IDNotExist {
        return salleRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Salle non trouvée"));
    }

    // Obtenir des salles par nom
    public List<Salle> getSallesByNom(String nomSalle) {
        return salleRepository.findByNomSalleContainingIgnoreCase(nomSalle);
    }

    // Obtenir des salles par localisation
    public List<Salle> getSallesByLocalisation(String localisation) {
        return salleRepository.findByLocalisationContainingIgnoreCase(localisation);
    }

    // Obtenir des salles par capacité minimale
    public List<Salle> getSallesByCapaciteMin(int capaciteMin) {
        return salleRepository.findByCapaciteMaxGreaterThanEqual(capaciteMin);
    }

    // Modifier une salle
    public Salle modifierSalle(UUID id, String nomSalle, String localisation, int capaciteMax) throws IDNotExist, ModificationImpossible {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Salle non trouvée"));

        // Vérifier si la salle peut être modifiée (par exemple, si elle n'a pas de réservations en cours)
        if (!peutEtreModifiee(salle)) {
            throw new ModificationImpossible("La salle ne peut pas être modifiée car elle a des réservations en cours");
        }

        salle.modifierSalle(nomSalle, localisation, capaciteMax);
        return salleRepository.save(salle);
    }

    // Supprimer une salle
    public void supprimerSalle(UUID id) throws IDNotExist, SuppressionImpossible {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Salle non trouvée"));

        // Vérifier si la salle peut être supprimée
        if (!salle.supprimerSalle()) {
            throw new SuppressionImpossible("La salle ne peut pas être supprimée car elle a des réservations associées");
        }

        salleRepository.deleteById(id);
    }

    // Vérifier si une salle peut être modifiée
    private boolean peutEtreModifiee(Salle salle) {
        // Une salle peut être modifiée si elle n'a pas de réservations en cours
        // Cette logique peut être adaptée selon les besoins spécifiques
        return salle.getReservations() == null || salle.getReservations().isEmpty();
    }
}
