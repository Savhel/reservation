package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.service.CoursService;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.service.EnseignantService;
import org.polytech.reservation.service.FormationService;
import org.polytech.reservation.service.PdfGeneratorServiceFormation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/formations")
public class FormationController {

    @Autowired
    private FormationService formationService;

    @Autowired
    private CoursService coursService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private PdfGeneratorServiceFormation pdfGeneratorService;

    // Créer une formation
    @PostMapping
    public ResponseEntity<Formation> creerFormation(
            @RequestParam String theme,
            @RequestParam int nombreDePlaces,
            @RequestParam String matriculeEnseignant) {
        try {
            Formation formation = formationService.creerFormation(theme, nombreDePlaces, matriculeEnseignant);
            return new ResponseEntity<>(formation, HttpStatus.CREATED);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer toutes les formations
    @GetMapping
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAllFormations();
        return new ResponseEntity<>(formations, HttpStatus.OK);
    }

    // Récupérer une formation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Formation> getFormationById(@PathVariable UUID id) {
        try {
            Formation formation = formationService.getFormationById(id);
            return new ResponseEntity<>(formation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer les formations par enseignant
    @GetMapping("/enseignant/{matricule}")
    public ResponseEntity<List<Formation>> getFormationsByEnseignant(@PathVariable String matricule) {
        try {
            List<Formation> formations = formationService.getFormationsByEnseignant(matricule);
            return new ResponseEntity<>(formations, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Modifier une formation
    @PutMapping("/{id}")
    public ResponseEntity<Formation> modifierFormation(
            @PathVariable UUID id,
            @RequestParam String theme,
            @RequestParam int nombreDePlaces,
            @RequestParam String matriculeEnseignant) {
        try {
            Formation formation = formationService.modifierFormation(id, theme, nombreDePlaces, matriculeEnseignant);
            return new ResponseEntity<>(formation, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Supprimer une formation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerFormation(@PathVariable UUID id) {
        try {
            formationService.supprimerFormation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SuppressionImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Récupérer le récapitulatif horaire d'une formation
    @GetMapping("/{id}/recap-horaire")
    public ResponseEntity<Object> getRecapHoraireFormation(@PathVariable UUID id) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(id);
            
            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(id);
            // Créer un objet pour le récapitulatif
            var recap = new Object() {

                // Calculer le nombre total d'heures
                int totalHeure = cours.stream().mapToInt(c -> c.getNbHeure()).sum();

                public final UUID idFormation = formation.getIdFormation();
                public final String theme = formation.getTheme();
                public final int nombreDePlaces = formation.getNombreDePlaces();
                public final String enseignantResponsable = formation.getEnseignantResponsable().getNom() + " " + formation.getEnseignantResponsable().getPrenom();
                public final int nombreCours = cours.size();
                public final int totalHeures = totalHeure;
                public final List<CoursDTO> listeCours = cours;
            };
            
            return new ResponseEntity<>(recap, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer le planning d'une formation
    @GetMapping("/{id}/planning")
    public ResponseEntity<List<CoursDTO>> getPlanningFormation(@PathVariable UUID id) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(id);
            
            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(id);
            // Le tri se fait côté client pour plus de flexibilité
            return new ResponseEntity<>(cours, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Ajouter un cours à une formation
    @PostMapping("/{id}/cours")
    public ResponseEntity<CoursDTO> ajouterCoursFormation(
            @PathVariable UUID id,
            @RequestBody Cours cours) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(id);
            
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(cours.getEnseignant().getMatricule());
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Vérifier que l'enseignant est autorisé à créer un cours dans cette formation
            // Soit il est responsable de la formation, soit il est l'enseignant assigné au cours
            if (!formation.getEnseignantResponsable().getMatricule().equals(enseignant.getMatricule())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            // Ajouter le cours à la formation
            cours.setFormation(formation);
            formation.ajouterCours(cours);
            
            CoursDTO coursDTO = coursService.createCours(cours);
            return new ResponseEntity<>(coursDTO, HttpStatus.CREATED);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Récupérer le récapitulatif horaire d'une formation au format PDF
    @GetMapping("/{id}/recap-horaire/pdf")
    public ResponseEntity<byte[]> getRecapHoraireFormationPdf(@PathVariable UUID id) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(id);

            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(id);

            // Calculer le nombre total d'heures
            int totalHeures = cours.stream().mapToInt(CoursDTO::getNbHeure).sum();

            // Générer le PDF
            byte[] pdfBytes = pdfGeneratorService.generateRecapHorairePdf(formation, cours, totalHeures);

            // Préparer les headers pour le téléchargement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "recap_horaire_" + formation.getTheme().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Récupérer le planning d'une formation au format PDF
    @GetMapping("/{id}/planning/pdf")
    public ResponseEntity<byte[]> getPlanningFormationPdf(@PathVariable UUID id) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(id);

            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(id);

            // Générer le PDF
            byte[] pdfBytes = pdfGeneratorService.generatePlanningPdf(formation, cours);

            // Préparer les headers pour le téléchargement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "planning_" + formation.getTheme().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}