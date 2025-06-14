package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.*;
import org.polytech.reservation.repositories.ReservationSalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationSalleService {

    private final ReservationSalleRepository reservationSalleRepository;
    private final SalleService salleService;
    private final EnseignantService enseignantService;
    private final CoursService coursService;

    @Autowired
    public ReservationSalleService(ReservationSalleRepository reservationSalleRepository,
                                   SalleService salleService,
                                   EnseignantService enseignantService,
                                   CoursService coursService) {
        this.reservationSalleRepository = reservationSalleRepository;
        this.salleService = salleService;
        this.enseignantService = enseignantService;
        this.coursService = coursService;
    }

    // Créer une réservation de salle
    public ReservationSalle creerReservation(String type, UUID idSalle, UUID idCours, String matriculeEnseignant,
                                            LocalDate dateReservation, LocalTime heureDebut, LocalTime heureFin) throws IDNotExist {
        // Vérifier que l'enseignant existe
        Enseignant enseignant = enseignantService.getEnseignantByMatricule(matriculeEnseignant);
        if (enseignant == null) {
            throw new IDNotExist("L'enseignant avec le matricule " + matriculeEnseignant + " n'existe pas.");
        }

        // Vérifier que la salle existe
        Salle salle = salleService.getSalleById(idSalle);
        if (salle == null) {
            throw new IDNotExist("La salle avec l'ID " + idSalle + " n'existe pas.");
        }

        // Vérifier que le cours existe
        Cours cours = coursService.getCoursById(idCours);
        if (cours == null) {
            throw new IDNotExist("Le cours avec l'ID " + idCours + " n'existe pas.");
        }

        // Vérifier la disponibilité de la salle
        if (!isSalleDisponible(salle, dateReservation, heureDebut, heureFin)) {
            throw new ModificationImpossible("La salle n'est pas disponible à cette date et heure.");
        }

        // Créer la réservation
        ReservationSalle reservation = new ReservationSalle();
        reservation.setType(type);
        reservation.setSalle(salle);
        reservation.setCours(cours);
        reservation.setEnseignant(enseignant);
        reservation.setDateReservation(dateReservation);
        reservation.setHeureDebut(heureDebut);
        reservation.setHeureFin(heureFin);
        reservation.setEstConfirmee(false);

        return reservationSalleRepository.save(reservation);
    }

    // Vérifier si une salle est disponible à une date et heure données
    private boolean isSalleDisponible(Salle salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        List<ReservationSalle> reservations = reservationSalleRepository.findBySalleAndDate(salle, date);
        
        for (ReservationSalle reservation : reservations) {
            // Vérifier si les plages horaires se chevauchent
            if (!(heureFin.isBefore(reservation.getHeureDebut()) || heureDebut.isAfter(reservation.getHeureFin()))) {
                return false; // Chevauchement détecté
            }
        }
        
        return true; // Aucun chevauchement, la salle est disponible
    }

    // Obtenir toutes les réservations
    public List<ReservationSalle> getAllReservations() {
        return reservationSalleRepository.findAll();
    }

    // Obtenir une réservation par son ID
    public ReservationSalle getReservationById(UUID id) throws IDNotExist {
        return reservationSalleRepository.findById(id)
                .orElseThrow(() -> new IDNotExist("La réservation avec l'ID " + id + " n'existe pas."));
    }

    // Obtenir les réservations par salle
    public List<ReservationSalle> getReservationsBySalle(UUID idSalle) throws IDNotExist {
        Salle salle = salleService.getSalleById(idSalle);
        if (salle == null) {
            throw new IDNotExist("La salle avec l'ID " + idSalle + " n'existe pas.");
        }
        return reservationSalleRepository.findBySalle(salle);
    }

    // Obtenir les réservations par enseignant
    public List<ReservationSalle> getReservationsByEnseignant(String matricule) throws IDNotExist {
        Enseignant enseignant = enseignantService.getEnseignantByMatricule(matricule);
        if (enseignant == null) {
            throw new IDNotExist("L'enseignant avec le matricule " + matricule + " n'existe pas.");
        }
        return reservationSalleRepository.findByEnseignant(enseignant);
    }

    // Obtenir les réservations par date
    public List<ReservationSalle> getReservationsByDate(LocalDate date) {
        return reservationSalleRepository.findByDateReservation(date);
    }

    // Obtenir les réservations par plage de dates
    public List<ReservationSalle> getReservationsByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return reservationSalleRepository.findByDateRange(dateDebut, dateFin);
    }

    // Modifier une réservation
    public ReservationSalle modifierReservation(UUID id, String type, UUID idSalle, UUID idCours,
                                              String matriculeEnseignant, LocalDate dateReservation,
                                              LocalTime heureDebut, LocalTime heureFin) throws IDNotExist, ModificationImpossible {
        // Vérifier que la réservation existe
        ReservationSalle reservation = getReservationById(id);

        // Vérifier que l'enseignant existe
        Enseignant enseignant = enseignantService.getEnseignantByMatricule(matriculeEnseignant);
        if (enseignant == null) {
            throw new IDNotExist("L'enseignant avec le matricule " + matriculeEnseignant + " n'existe pas.");
        }

        // Vérifier que la salle existe
        Salle salle = salleService.getSalleById(idSalle);
        if (salle == null) {
            throw new IDNotExist("La salle avec l'ID " + idSalle + " n'existe pas.");
        }

        // Vérifier que le cours existe
        Cours cours = coursService.getCoursById(idCours);
        if (cours == null) {
            throw new IDNotExist("Le cours avec l'ID " + idCours + " n'existe pas.");
        }

        // Si la salle ou la date/heure a changé, vérifier la disponibilité
        if (!reservation.getSalle().equals(salle) ||
            !reservation.getDateReservation().equals(dateReservation) ||
            !reservation.getHeureDebut().equals(heureDebut) ||
            !reservation.getHeureFin().equals(heureFin)) {
            
            if (!isSalleDisponible(salle, dateReservation, heureDebut, heureFin)) {
                throw new ModificationImpossible("La salle n'est pas disponible à cette date et heure.");
            }
        }

        // Mettre à jour la réservation
        reservation.setType(type);
        reservation.setSalle(salle);
        reservation.setCours(cours);
        reservation.setEnseignant(enseignant);
        reservation.setDateReservation(dateReservation);
        reservation.setHeureDebut(heureDebut);
        reservation.setHeureFin(heureFin);

        return reservationSalleRepository.save(reservation);
    }

    // Confirmer une réservation
    public ReservationSalle confirmerReservation(UUID id) throws IDNotExist {
        ReservationSalle reservation = getReservationById(id);
        reservation.setEstConfirmee(true);
        return reservationSalleRepository.save(reservation);
    }

    // Supprimer une réservation
    public void supprimerReservation(UUID id) throws IDNotExist, SuppressionImpossible {
        ReservationSalle reservation = getReservationById(id);
        
        // Vérifier si la réservation est déjà confirmée et si la date est passée
        if (reservation.isEstConfirmee() && reservation.getDateReservation().isBefore(LocalDate.now())) {
            throw new SuppressionImpossible("Impossible de supprimer une réservation confirmée dont la date est passée.");
        }
        
        reservationSalleRepository.delete(reservation);
    }
}