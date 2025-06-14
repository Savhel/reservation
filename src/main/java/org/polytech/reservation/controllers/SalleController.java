package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Salle;
import org.polytech.reservation.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salles")
public class SalleController {

    @Autowired
    private SalleService salleService;

    // Créer une salle
    @PostMapping
    public ResponseEntity<Salle> creerSalle(
            @RequestParam String nomSalle,
            @RequestParam String localisation,
            @RequestParam int capaciteMax) {
        Salle salle = salleService.creerSalle(nomSalle, localisation, capaciteMax);
        return new ResponseEntity<>(salle, HttpStatus.CREATED);
    }

    // Obtenir toutes les salles
    @GetMapping
    public ResponseEntity<List<Salle>> getAllSalles() {
        List<Salle> salles = salleService.getAllSalles();
        return new ResponseEntity<>(salles, HttpStatus.OK);
    }

    // Obtenir une salle par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Salle> getSalleById(@PathVariable UUID id) {
        try {
            Salle salle = salleService.getSalleById(id);
            return new ResponseEntity<>(salle, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir des salles par nom
    @GetMapping("/nom/{nomSalle}")
    public ResponseEntity<List<Salle>> getSallesByNom(@PathVariable String nomSalle) {
        List<Salle> salles = salleService.getSallesByNom(nomSalle);
        return new ResponseEntity<>(salles, HttpStatus.OK);
    }

    // Obtenir des salles par localisation
    @GetMapping("/localisation/{localisation}")
    public ResponseEntity<List<Salle>> getSallesByLocalisation(@PathVariable String localisation) {
        List<Salle> salles = salleService.getSallesByLocalisation(localisation);
        return new ResponseEntity<>(salles, HttpStatus.OK);
    }

    // Obtenir des salles par capacité minimale
    @GetMapping("/capacite/{capaciteMin}")
    public ResponseEntity<List<Salle>> getSallesByCapaciteMin(@PathVariable int capaciteMin) {
        List<Salle> salles = salleService.getSallesByCapaciteMin(capaciteMin);
        return new ResponseEntity<>(salles, HttpStatus.OK);
    }

    // Modifier une salle
    @PutMapping("/{id}")
    public ResponseEntity<Salle> modifierSalle(
            @PathVariable UUID id,
            @RequestParam String nomSalle,
            @RequestParam String localisation,
            @RequestParam int capaciteMax) {
        try {
            Salle salle = salleService.modifierSalle(id, nomSalle, localisation, capaciteMax);
            return new ResponseEntity<>(salle, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Supprimer une salle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerSalle(@PathVariable UUID id) {
        try {
            salleService.supprimerSalle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}