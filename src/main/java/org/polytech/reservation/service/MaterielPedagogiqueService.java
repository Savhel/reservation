package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.repositories.ReservationMaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MaterielPedagogiqueService {

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;
    @Autowired
    private ReservationMaterielRepository reservationMaterielRepository;
    

    // Créer un matériel
    public MaterielPedagogique creerMateriel(String nom, String marque) {
        MaterielPedagogique materiel = new MaterielPedagogique(nom, marque);
        materiel.setReservationMateriels(new ArrayList<>());
        return materielPedagogiqueRepository.save(materiel);
    }

    // Obtenir tous les matériels
    public List<MaterielPedagogique> getAllMateriels() {
        return materielPedagogiqueRepository.findAll();
    }

    // Obtenir un matériel par son ID
    public MaterielPedagogique getMaterielById(UUID id) throws IDNotExist {
        return materielPedagogiqueRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Matériel non trouvé"));
    }

    // Obtenir des matériels par marque
    public List<MaterielPedagogique> getMaterielsByMarque(String marque) {
        return materielPedagogiqueRepository.findByMarque(marque);
    }

    // Modifier un matériel
    public MaterielPedagogique modifierMateriel(UUID id, String nom, String marque) throws IDNotExist, ModificationImpossible {
        MaterielPedagogique materiel = materielPedagogiqueRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Matériel non trouvé"));

        // Vérifier si le matériel peut être modifié (par exemple, s'il n'est pas réservé)
        if (!peutEtreModifie(materiel)) {
            throw new ModificationImpossible("Le matériel ne peut pas être modifié car il est actuellement réservé");
        }

        materiel.setNom(nom);
        materiel.setMarque(marque);

        return materielPedagogiqueRepository.save(materiel);
    }

    // Supprimer un matériel
    public void supprimerMateriel(UUID id) throws IDNotExist, SuppressionImpossible {
        MaterielPedagogique materiel = materielPedagogiqueRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Matériel non trouvé"));

        // Vérifier si le matériel peut être supprimé
        if (!peutEtreSupprime(materiel)) {
            throw new SuppressionImpossible("Le matériel ne peut pas être supprimé car il est associé à des réservations");
        }

        materielPedagogiqueRepository.deleteById(id);
    }

    // Vérifier si un matériel peut être modifié
    private boolean peutEtreModifie(MaterielPedagogique materiel) {
        // Un matériel peut être modifié s'il n'a pas de réservations confirmées
        List<ReservationMateriel> reservations = reservationMaterielRepository.getReservationMaterielByMateriel(materiel);
        for (ReservationMateriel reservation : reservations) {
            if (reservation.isConfirmee()) {
                return false;
            }
        }
        return true;
    }

    // Vérifier si un matériel peut être supprimé
    private boolean peutEtreSupprime(MaterielPedagogique materiel) {
        // Un matériel peut être supprimé s'il n'a pas de réservations
        return reservationMaterielRepository.getReservationMaterielByMateriel(materiel).isEmpty();
    }
}
