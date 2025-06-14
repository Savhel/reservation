package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Etudiant;
import org.polytech.reservation.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // Créer un étudiant
    @PostMapping
    public ResponseEntity<Etudiant> creerEtudiant(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String motDePasse,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaiss,
            @RequestParam String lieuNaiss,
            @RequestParam String mail,
            @RequestParam String niveau,
            @RequestParam String filiere) {
        Etudiant etudiant = etudiantService.creerEtudiant(
                nom, prenom, motDePasse, dateNaiss, lieuNaiss, mail, niveau, filiere);
        return new ResponseEntity<>(etudiant, HttpStatus.CREATED);
    }

    // Récupérer tous les étudiants
    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer un étudiant par son matricule
    @GetMapping("/{matricule}")
    public ResponseEntity<Etudiant> getEtudiantByMatricule(@PathVariable String matricule) {
        try {
            Etudiant etudiant = etudiantService.getEtudiantByMatricule(matricule);
            return new ResponseEntity<>(etudiant, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer des étudiants par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByNom(@PathVariable String nom) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNom(nom);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer des étudiants par prénom
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByPrenom(@PathVariable String prenom) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByPrenom(prenom);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer des étudiants par niveau
    @GetMapping("/niveau/{niveau}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByNiveau(@PathVariable String niveau) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveau(niveau);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer des étudiants par filière
    @GetMapping("/filiere/{filiere}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByFiliere(@PathVariable String filiere) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByFiliere(filiere);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer des étudiants par nom et prénom
    @GetMapping("/recherche")
    public ResponseEntity<List<Etudiant>> getEtudiantsByNomAndPrenom(
            @RequestParam String nom,
            @RequestParam String prenom) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNomAndPrenom(nom, prenom);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Récupérer des étudiants par niveau et filière
    @GetMapping("/recherche/formation")
    public ResponseEntity<List<Etudiant>> getEtudiantsByNiveauAndFiliere(
            @RequestParam String niveau,
            @RequestParam String filiere) {
        List<Etudiant> etudiants = etudiantService.getEtudiantsByNiveauAndFiliere(niveau, filiere);
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    // Modifier un étudiant
    @PutMapping("/{matricule}")
    public ResponseEntity<Etudiant> modifierEtudiant(
            @PathVariable String matricule,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String motDePasse,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaiss,
            @RequestParam String lieuNaiss,
            @RequestParam String mail,
            @RequestParam String niveau,
            @RequestParam String filiere) {
        try {
            Etudiant etudiant = etudiantService.modifierEtudiant(
                    matricule, nom, prenom, motDePasse, dateNaiss, lieuNaiss, mail, niveau, filiere);
            return new ResponseEntity<>(etudiant, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un étudiant
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> supprimerEtudiant(@PathVariable String matricule) {
        try {
            etudiantService.supprimerEtudiant(matricule);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}