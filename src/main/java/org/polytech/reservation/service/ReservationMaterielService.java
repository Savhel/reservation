package org.polytech.reservation.service;

import jakarta.persistence.EntityNotFoundException;
import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.repositories.EnseignantRepository;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.repositories.ReservationMaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationMaterielService {

    private final ReservationMaterielRepository reservationMaterielRepository;
    private final MaterielPedagogiqueRepository materielRepository;
    private final CoursRepository coursRepository;
    private final EnseignantRepository enseignantRepository;

    @Autowired
    public ReservationMaterielService(ReservationMaterielRepository reservationMaterielRepository,
                                      MaterielPedagogiqueRepository materielRepository,
                                      CoursRepository coursRepository,
                                      EnseignantRepository enseignantRepository) {
        this.reservationMaterielRepository = reservationMaterielRepository;
        this.materielRepository = materielRepository;
        this.coursRepository = coursRepository;
        this.enseignantRepository = enseignantRepository;
    }

    // Créer une réservation
    public ReservationMateriel creerReservation(String type, UUID idMateriel, UUID idCours,
                                                String matriculeEnseignant, LocalDate dateReservation,
                                                LocalTime heureDebut, LocalTime heureFin) throws IDNotExist, ModificationImpossible {
        // Vérifier que le matériel existe
        MaterielPedagogique materiel = materielRepository.findById(idMateriel)
                .orElseThrow(() -> new IDNotExist("Matériel non trouvé"));

        // Vérifier que le cours existe
        Cours cours = coursRepository.findById(idCours)
                .orElseThrow(() -> new IDNotExist("Cours non trouvé"));

        // Vérifier que l'enseignant existe
        Enseignant enseignant = enseignantRepository.findByMatricule(matriculeEnseignant)
                .orElseThrow(() -> new IDNotExist("Enseignant non trouvé"));

        // Vérifier la disponibilité du matériel
//        if (!estMaterielDisponible(idMateriel, dateReservation, heureDebut, heureFin)) {
//            throw new ModificationImpossible("Le matériel n'est pas disponible pour cette période");
//        }

        // Créer la réservation
        ReservationMateriel reservation = new ReservationMateriel();
        reservation.setType(type);
        reservation.setMateriel(materiel);
        reservation.setCours(cours);
        reservation.setEnseignant(enseignant);
        reservation.setDateReservation(dateReservation);
        reservation.setHeureDebut(heureDebut);
        reservation.setHeureFin(heureFin);
        reservation.setConfirmee(false);

        return reservationMaterielRepository.save(reservation);
    }

    // Vérifier la disponibilité du matériel
//    public boolean estMaterielDisponible(UUID idMateriel, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
//        List<ReservationMateriel> reservations = reservationMaterielRepository.findByMateriel_IdMaterielAndDateReservation(idMateriel, date);
//
//        for (ReservationMateriel reservation : reservations) {
//            // Vérifier si les plages horaires se chevauchent
//            if ((heureDebut.isBefore(reservation.getHeureFin()) || heureDebut.equals(reservation.getHeureFin())) &&
//                    (heureFin.isAfter(reservation.getHeureDebut()) || heureFin.equals(reservation.getHeureDebut()))) {
//                return false;
//            }
//        }
//
//        return true;
//    }

    // Obtenir toutes les réservations
    public List<ReservationMateriel> getAllReservations() {
        return reservationMaterielRepository.findAll();
    }

    // Obtenir une réservation par son ID
    public ReservationMateriel getReservationById(UUID id) throws IDNotExist {
        return reservationMaterielRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Réservation non trouvée"));
    }

    // Obtenir les réservations par matériel
    public List<ReservationMateriel> getReservationsByMateriel(UUID idMateriel) throws IDNotExist {
        if (!materielRepository.existsById(idMateriel)) {
            throw new IDNotExist("Matériel non trouvé");
        }
        return reservationMaterielRepository.findByMaterielId(idMateriel);
    }

    // Obtenir les réservations par enseignant
    public List<ReservationMateriel> getReservationsByEnseignant(String matricule) throws IDNotExist {
        if (!enseignantRepository.existsByMatricule(matricule)) {
            throw new IDNotExist("Enseignant non trouvé");
        }
        return reservationMaterielRepository.findByEnseignantMatricule(matricule);
    }

    // Obtenir les réservations par date
    public List<ReservationMateriel> getReservationsByDate(LocalDate date) {
        return reservationMaterielRepository.findByDateReservation(date);
    }

    // Obtenir les réservations par plage de dates
    public List<ReservationMateriel> getReservationsByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return reservationMaterielRepository.findByDateReservationBetween(dateDebut, dateFin);
    }

    // Modifier une réservation
//    public ReservationMateriel modifierReservation(UUID id, String type, UUID idMateriel, UUID idCours,
//                                                   String matriculeEnseignant, LocalDate dateReservation,
//                                                   LocalTime heureDebut, LocalTime heureFin) throws IDNotExist, ModificationImpossible {
//        // Vérifier que la réservation existe
//        ReservationMateriel reservation = reservationMaterielRepository.findById(id)
//                .orElseThrow(() -> new IDNotExist("Réservation non trouvée"));
//
//        // Si la réservation est déjà confirmée, on ne peut pas la modifier
//        if (reservation.isConfirmee()) {
//            throw new ModificationImpossible("Impossible de modifier une réservation confirmée");
//        }
//
//        // Vérifier que le matériel existe
//        MaterielPedagogique materiel = materielRepository.findById(idMateriel)
//                .orElseThrow(() -> new IDNotExist("Matériel non trouvé"));
//
//        // Vérifier que le cours existe
//        Cours cours = coursRepository.findById(idCours)
//                .orElseThrow(() -> new IDNotExist("Cours non trouvé"));
//
//        // Vérifier que l'enseignant existe
//        Enseignant enseignant = enseignantRepository.findByMatricule(matriculeEnseignant)
//                .orElseThrow(() -> new IDNotExist("Enseignant non trouvé"));
//
//
//        if (!reservation.getMateriel().getIdMateriel().equals(idMateriel) ||
//                !reservation.getDateReservation().equals(dateReservation) ||
//                !reservation.getHeureDebut().equals(heureDebut) ||
//                !reservation.getHeureFin().equals(heureFin)) {
//
//            Optional<MaterielPedagogique> optionalMateriel = materielRepository.findById(idMateriel);
//            if (optionalMateriel.isEmpty()) {
//                throw new EntityNotFoundException("Matériel introuvable");
//            }
//
//            MaterielPedagogique materiel1 = optionalMateriel.get();
//
//// Filtrer uniquement les réservations à la date souhaitée
//            List<ReservationMateriel> reservations = materiel.getReservationMateriels().stream()
//                    .filter(res -> res.getDateReservation().equals(dateReservation))
//                    .toList();
//
//            for (ReservationMateriel res : reservations) {
//                if (!res.getIdReservation().equals(id) && // Exclure la réservation actuelle
//                        (heureDebut.isBefore(res.getHeureFin()) || heureDebut.equals(res.getHeureFin())) &&
//                        (heureFin.isAfter(res.getHeureDebut()) || heureFin.equals(res.getHeureDebut()))) {
//                    throw new ModificationImpossible("Le matériel n'est pas disponible pour cette période");
//                }
//            }
//
//        }
//
//        // Mettre à jour la réservation
//        reservation.setType(type);
//        reservation.setMateriel(materiel);
//        reservation.setCours(cours);
//        reservation.setEnseignant(enseignant);
//        reservation.setDateReservation(dateReservation);
//        reservation.setHeureDebut(heureDebut);
//        reservation.setHeureFin(heureFin);
//
//        return reservationMaterielRepository.save(reservation);
//    }

    // Confirmer une réservation
    public ReservationMateriel confirmerReservation(UUID id) throws IDNotExist {
        ReservationMateriel reservation = reservationMaterielRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Réservation non trouvée"));

        reservation.setConfirmee(true);
        return reservationMaterielRepository.save(reservation);
    }

    // Supprimer une réservation
    public void supprimerReservation(UUID id) throws IDNotExist, SuppressionImpossible {
        ReservationMateriel reservation = reservationMaterielRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("Réservation non trouvée"));

        // Si la réservation est confirmée, on ne peut pas la supprimer
        if (reservation.isConfirmee()) {
            throw new SuppressionImpossible("Impossible de supprimer une réservation confirmée");
        }

        reservationMaterielRepository.deleteById(id);
    }
}
