package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.models.Ordinateur;
import org.polytech.reservation.models.VideoProjecteur;
import org.polytech.reservation.repositories.OrdinateurRepository;
import org.polytech.reservation.repositories.VideoProjecteurRepository;
import org.polytech.reservation.service.MaterielPedagogiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/materiels")
public class MaterielPedagogiqueController {

    @Autowired
    private MaterielPedagogiqueService materielPedagogiqueService;
    @Autowired
    private OrdinateurRepository ordinateurRepository;

    @Autowired
    private VideoProjecteurRepository videoProjecteurRepository;


    // Créer un matériel
    @PostMapping
    public ResponseEntity<MaterielPedagogique> creerMaterielOrdinateur(@RequestParam String nom, @RequestParam String marque, Ordinateur ordinateur) {
        MaterielPedagogique materiel = materielPedagogiqueService.creerMateriel(nom, marque);
        ordinateur.setMateriel(materiel);
        ordinateurRepository.save(ordinateur);
        return new ResponseEntity<>(materiel, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<MaterielPedagogique> creerMaterielVideoProjecteur(@RequestParam String nom, @RequestParam String marque, VideoProjecteur videoProjecteur) {
        MaterielPedagogique materiel = materielPedagogiqueService.creerMateriel(nom, marque);
        videoProjecteur.setMateriel(materiel);
        videoProjecteurRepository.save(videoProjecteur);
        return new ResponseEntity<>(materiel, HttpStatus.CREATED);
    }

    // Obtenir tous les matériels
    @GetMapping
    public ResponseEntity<List<MaterielPedagogique>> getAllMateriels() {
        List<MaterielPedagogique> materiels = materielPedagogiqueService.getAllMateriels();
        return new ResponseEntity<>(materiels, HttpStatus.OK);
    }

    // Obtenir un matériel par son ID
    @GetMapping("/{id}")
    public ResponseEntity<MaterielPedagogique> getMaterielById(@PathVariable UUID id) {
        try {
            MaterielPedagogique materiel = materielPedagogiqueService.getMaterielById(id);
            return new ResponseEntity<>(materiel, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtenir des matériels par marque
    @GetMapping("/marque/{marque}")
    public ResponseEntity<List<MaterielPedagogique>> getMaterielsByMarque(@PathVariable String marque) {
        List<MaterielPedagogique> materiels = materielPedagogiqueService.getMaterielsByMarque(marque);
        return new ResponseEntity<>(materiels, HttpStatus.OK);
    }

    // Modifier un matériel
    @PutMapping("/{id}")
    public ResponseEntity<MaterielPedagogique> modifierMateriel(
            @PathVariable UUID id,
            @RequestParam String nom,
            @RequestParam String marque) {
        try {
            MaterielPedagogique materiel = materielPedagogiqueService.modifierMateriel(id, nom, marque);
            return new ResponseEntity<>(materiel, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer un matériel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerMateriel(@PathVariable UUID id) {
        try {
            materielPedagogiqueService.supprimerMateriel(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}