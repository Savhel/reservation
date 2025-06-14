package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    // Créer un enseignant
    @PostMapping
    public ResponseEntity<Enseignant> creerEnseignant(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String motDePasse,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaiss,
            @RequestParam String lieuNaiss,
            @RequestParam String mail,
            @RequestParam String tel,
            @RequestParam String grade) {
        Enseignant enseignant = enseignantService.creerEnseignant(
                nom, prenom, motDePasse, dateNaiss, lieuNaiss, mail, tel, grade);
        return new ResponseEntity<>(enseignant, HttpStatus.CREATED);
    }

    // Obtenir tous les enseignants
    @GetMapping
    public ResponseEntity<List<Enseignant>> getAllEnseignants() {
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    // Obtenir un enseignant par son matricule
    @GetMapping("/{matricule}")
    public ResponseEntity<Enseignant> getEnseignantByMatricule(@PathVariable String matricule) {
        try {
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matricule);
            return new ResponseEntity<>(enseignant, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir des enseignants par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Enseignant>> getEnseignantsByNom(@PathVariable String nom) {
        List<Enseignant> enseignants = enseignantService.getEnseignantsByNom(nom);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    // Obtenir des enseignants par prénom
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<List<Enseignant>> getEnseignantsByPrenom(@PathVariable String prenom) {
        List<Enseignant> enseignants = enseignantService.getEnseignantsByPrenom(prenom);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    // Obtenir des enseignants par grade
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<Enseignant>> getEnseignantsByGrade(@PathVariable String grade) {
        List<Enseignant> enseignants = enseignantService.getEnseignantsByGrade(grade);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    // Obtenir des enseignants par nom et prénom
    @GetMapping("/recherche")
    public ResponseEntity<List<Enseignant>> getEnseignantsByNomAndPrenom(
            @RequestParam String nom,
            @RequestParam String prenom) {
        List<Enseignant> enseignants = enseignantService.getEnseignantsByNomAndPrenom(nom, prenom);
        return new ResponseEntity<>(enseignants, HttpStatus.OK);
    }

    // Modifier un enseignant
    @PutMapping("/{matricule}")
    public ResponseEntity<Enseignant> modifierEnseignant(
            @PathVariable String matricule,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String motDePasse,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaiss,
            @RequestParam String lieuNaiss,
            @RequestParam String mail,
            @RequestParam String tel,
            @RequestParam String grade) {
        try {
            Enseignant enseignant = enseignantService.modifierEnseignant(
                    matricule, nom, prenom, motDePasse, dateNaiss, lieuNaiss, mail, tel, grade);
            return new ResponseEntity<>(enseignant, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Supprimer un enseignant
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> supprimerEnseignant(@PathVariable String matricule) {
        try {
            enseignantService.supprimerEnseignant(matricule);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}