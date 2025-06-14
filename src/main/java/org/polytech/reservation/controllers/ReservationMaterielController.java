package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.service.ReservationMaterielService;
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
@RequestMapping("/api/reservations/materiels")
public class ReservationMaterielController {

    @Autowired
    private ReservationMaterielService reservationMaterielService;

    // Créer une réservation
    @PostMapping
    public ResponseEntity<ReservationMateriel> creerReservation(
            @RequestParam String type,
            @RequestParam UUID idMateriel,
            @RequestParam UUID idCours,
            @RequestParam String matriculeEnseignant,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        try {
            ReservationMateriel reservation = reservationMaterielService.creerReservation(
                    type, idMateriel, idCours, matriculeEnseignant, dateReservation, heureDebut, heureFin);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (IDNotExist | ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtenir toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationMateriel>> getAllReservations() {
        List<ReservationMateriel> reservations = reservationMaterielService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir une réservation par son ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationMateriel> getReservationById(@PathVariable UUID id) {
        try {
            ReservationMateriel reservation = reservationMaterielService.getReservationById(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par matériel
    @GetMapping("/materiel/{idMateriel}")
    public ResponseEntity<List<ReservationMateriel>> getReservationsByMateriel(@PathVariable UUID idMateriel) {
        try {
            List<ReservationMateriel> reservations = reservationMaterielService.getReservationsByMateriel(idMateriel);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par enseignant
    @GetMapping("/enseignant/{matricule}")
    public ResponseEntity<List<ReservationMateriel>> getReservationsByEnseignant(@PathVariable String matricule) {
        try {
            List<ReservationMateriel> reservations = reservationMaterielService.getReservationsByEnseignant(matricule);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir les réservations par date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ReservationMateriel>> getReservationsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ReservationMateriel> reservations = reservationMaterielService.getReservationsByDate(date);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Obtenir les réservations par plage de dates
    @GetMapping("/date-range")
    public ResponseEntity<List<ReservationMateriel>> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<ReservationMateriel> reservations = reservationMaterielService.getReservationsByDateRange(dateDebut, dateFin);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    // Modifier une réservation
    @PutMapping("/{id}")
    public ResponseEntity<ReservationMateriel> modifierReservation(
            @PathVariable UUID id,
            @RequestParam String type,
            @RequestParam UUID idMateriel,
            @RequestParam UUID idCours,
            @RequestParam String matriculeEnseignant,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin) {
        try {
            ReservationMateriel reservation = reservationMaterielService.modifierReservation(
                    id, type, idMateriel, idCours, matriculeEnseignant, dateReservation, heureDebut, heureFin);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Confirmer une réservation
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ReservationMateriel> confirmerReservation(@PathVariable UUID id) {
        try {
            ReservationMateriel reservation = reservationMaterielService.confirmerReservation(id);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerReservation(@PathVariable UUID id) {
        try {
            reservationMaterielService.supprimerReservation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}