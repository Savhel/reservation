package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.ReservationSalle;
import org.polytech.reservation.service.ReservationSalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations/salles")
public class ReservationSalleController {

    @Autowired
    private ReservationSalleService reservationSalleService;

    // Créer une réservation
    @PostMapping
    public ResponseEntity<ReservationSalle> creerReservation(
            @RequestParam String type,
            @RequestParam UUID idSalle,
            @RequestParam UUID idCours,
            @RequestParam String matriculeEnseignant,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        try {
            ReservationSalle reservation = reservationSalleService.creerReservation(
                    type, idSalle, idCours, matriculeEnseignant, dateReservation, heureDebut, heureFin);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (IDNotExist | ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtenir toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationSalle>> getAllReservations() {
        List<ReservationSalle> reservations = reservationSalleService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir une réservation par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationSalle> getReservationById(@PathVariable UUID id) {
        try {
            ReservationSalle reservation = reservationSalleService.getReservationById(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par salle
    @GetMapping("/salle/{idSalle}")
    public ResponseEntity<List<ReservationSalle>> getReservationsBySalle(@PathVariable UUID idSalle) {
        try {
            List<ReservationSalle> reservations = reservationSalleService.getReservationsBySalle(idSalle);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par enseignant
    @GetMapping("/enseignant/{matricule}")
    public ResponseEntity<List<ReservationSalle>> getReservationsByEnseignant(@PathVariable String matricule) {
        try {
            List<ReservationSalle> reservations = reservationSalleService.getReservationsByEnseignant(matricule);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationSalle>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ReservationSalle> reservations = reservationSalleService.getReservationsByDate(date);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir les réservations par plage de dates
    @GetMapping("/date-range")
    public ResponseEntity<List<ReservationSalle>> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<ReservationSalle> reservations = reservationSalleService.getReservationsByDateRange(dateDebut, dateFin);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Modifier une réservation
    @PutMapping("/{id}")
    public ResponseEntity<ReservationSalle> modifierReservation(
            @PathVariable UUID id,
            @RequestParam String type,
            @RequestParam UUID idSalle,
            @RequestParam UUID idCours,
            @RequestParam String matriculeEnseignant,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        try {
            ReservationSalle reservation = reservationSalleService.modifierReservation(
                    id, type, idSalle, idCours, matriculeEnseignant, dateReservation, heureDebut, heureFin);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Confirmer une réservation
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ReservationSalle> confirmerReservation(@PathVariable UUID id) {
        try {
            ReservationSalle reservation = reservationSalleService.confirmerReservation(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(@PathVariable UUID id) {
        try {
            reservationSalleService.supprimerReservation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}