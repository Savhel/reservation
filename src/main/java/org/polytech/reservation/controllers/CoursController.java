package org.polytech.reservation.controllers;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.Formation;
import org.polytech.reservation.service.CoursService;
import org.polytech.reservation.service.DTO.classes.CoursDTO;
import org.polytech.reservation.service.EnseignantService;
import org.polytech.reservation.service.FormationService;
import org.polytech.reservation.service.PdfGeneratorServiceEnseignant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cours")
public class CoursController {

    @Autowired
    private CoursService coursService;
    @Autowired
    private EnseignantService enseignantService;
    @Autowired
    private FormationService formationService;

    @Autowired
    private PdfGeneratorServiceEnseignant pdfService;


    // Créer un cours
    @PostMapping
    public ResponseEntity<CoursDTO> creerCours(
            @RequestParam String sujet,
            @RequestParam int nombreHeures,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jour,
            @RequestParam String heure,
            @RequestParam String matriculeEnseignant) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matriculeEnseignant);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Créer le cours
            Cours cours = Cours.ajouterCours(sujet, nombreHeures, jour, Time.valueOf(heure), enseignant);
            CoursDTO coursDTO = coursService.createCours(cours);
            return new ResponseEntity<>(coursDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Créer un cours dans une formation
    @PostMapping("/formation/{idFormation}")
    public ResponseEntity<CoursDTO> creerCoursDansFormation(
            @PathVariable UUID idFormation,
            @RequestParam String sujet,
            @RequestParam int nombreHeures,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jour,
            @RequestParam String heure,
            @RequestParam String matriculeEnseignant) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matriculeEnseignant);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(idFormation);
            if (formation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Vérifier que l'enseignant est autorisé à créer un cours dans cette formation
            // Soit il est responsable de la formation, soit il est l'enseignant assigné au cours
            if (!formation.getEnseignantResponsable().getMatricule().equals(matriculeEnseignant)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            // Créer le cours et l'ajouter à la formation
            Cours cours = Cours.ajouterCours(sujet, nombreHeures, jour, Time.valueOf(heure), enseignant);
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

    // Récupérer tous les cours
    @GetMapping
    public ResponseEntity<List<CoursDTO>> getAllCours() {
        List<CoursDTO> cours = coursService.readAllCours();
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

    // Récupérer un cours par ID
    @GetMapping("/{id}")
    public ResponseEntity<CoursDTO> getCoursById(@PathVariable UUID id) {
        try {
            CoursDTO cours = coursService.readCours(id);
            if (cours == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cours, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer les cours par enseignant
    @GetMapping("/enseignant/{matricule}")
    public ResponseEntity<List<CoursDTO>> getCoursByEnseignant(@PathVariable String matricule) {
        try {
            List<CoursDTO> cours = coursService.readAllCoursByEnseignant(matricule);
            return new ResponseEntity<>(cours, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer les cours par formation
    @GetMapping("/formation/{idFormation}")
    public ResponseEntity<List<CoursDTO>> getCoursByFormation(@PathVariable UUID idFormation) {
        try {
            List<CoursDTO> cours = coursService.readAllCoursByFormation(idFormation);
            return new ResponseEntity<>(cours, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer le récapitulatif horaire d'une formation
    @GetMapping("/formation/{idFormation}/recap-horaire")
    public ResponseEntity<Object> getRecapHoraireFormation(@PathVariable UUID idFormation) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(idFormation);
            if (formation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(idFormation);
            

            // Créer un objet pour le récapitulatif
            var recap = new Object() {

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

    // Récupérer le récapitulatif horaire d'un enseignant
    @GetMapping("/enseignant/{matricule}/recap-horaire")
    public ResponseEntity<Object> getRecapHoraireEnseignant(@PathVariable String matricule) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matricule);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Récupérer tous les cours de l'enseignant
            List<CoursDTO> cours = coursService.readAllCoursByEnseignant(matricule);
            

            
            // Créer un objet pour le récapitulatif
            var recap = new Object() {
                // Calculer le nombre total d'heures
                int totalHeure = cours.stream().mapToInt(c -> c.getNbHeure()).sum();

                public final String matricule = enseignant.getMatricule();
                public final String nom = enseignant.getNom();
                public final String prenom = enseignant.getPrenom();
                public final String grade = enseignant.getGrade();
                public final int nombreCours = cours.size();
                public final int totalHeures = totalHeure;
                public final List<CoursDTO> listeCours = cours;
            };
            
            return new ResponseEntity<>(recap, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer le planning des formations
    @GetMapping("/planning")
    public ResponseEntity<List<CoursDTO>> getPlanningFormations() {
        // Récupérer tous les cours triés par date et heure
        // on fait le tri devant
        List<CoursDTO> cours = coursService.readAllCours();
        return new ResponseEntity<>(cours, HttpStatus.OK);
    }

    // Récupérer le planning d'une formation spécifique
    @GetMapping("/planning/formation/{idFormation}")
    public ResponseEntity<List<CoursDTO>> getPlanningFormation(@PathVariable UUID idFormation) {
        try {
            // Vérifier que la formation existe
            Formation formation = formationService.getFormationById(idFormation);
            if (formation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Récupérer tous les cours de la formation
            List<CoursDTO> cours = coursService.readAllCoursByFormation(idFormation);
            // Le tri se fait côté client pour plus de flexibilité
            return new ResponseEntity<>(cours, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Modifier un cours
    @PutMapping("/{id}")
    public ResponseEntity<CoursDTO> modifierCours(
            @PathVariable UUID id,
            @RequestParam String sujet,
            @RequestParam int nombreHeures,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate jour,
            @RequestParam String heure,
            @RequestParam String matriculeEnseignant) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matriculeEnseignant);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Récupérer le cours existant
            CoursDTO existingCours = coursService.readCours(id);
            if (existingCours == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            // Créer le cours modifié
            Cours cours = new Cours();
            cours.setId(id);
            cours.setSujet(sujet);
            cours.setNombreHeures(nombreHeures);
            cours.setJour(jour);
            cours.setHeure(Time.valueOf(heure));
            cours.setEnseignant(enseignant);
            
            // Mettre à jour le cours
            CoursDTO updatedCours = coursService.updateCours(cours, id);
            return new ResponseEntity<>(updatedCours, HttpStatus.OK);
        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ModificationImpossible e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Supprimer un cours
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCours(@PathVariable UUID id) {
        try {
            boolean deleted = coursService.deleteCours(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/enseignant/{matricule}/recap-horaire/pdf")
    public ResponseEntity<byte[]> getRecapHoraireEnseignantPdf(@PathVariable String matricule) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matricule);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Récupérer tous les cours de l'enseignant
            List<CoursDTO> cours = coursService.readAllCoursByEnseignant(matricule);

            // Générer le PDF
            byte[] pdfBytes = pdfService.generateRecapitulatifHorairePdf(enseignant, cours);

            // Configurer les en-têtes HTTP pour le téléchargement du PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("recap_horaire_" + enseignant.getMatricule() + ".pdf")
                    .build());
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Méthode pour visualiser le PDF dans le navigateur (optionnel)
    @GetMapping("/enseignant/{matricule}/recap-horaire/pdf/view")
    public ResponseEntity<byte[]> viewRecapHoraireEnseignantPdf(@PathVariable String matricule) {
        try {
            // Vérifier que l'enseignant existe
            Enseignant enseignant = enseignantService.getEnseignantByMatricule(matricule);
            if (enseignant == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Récupérer tous les cours de l'enseignant
            List<CoursDTO> cours = coursService.readAllCoursByEnseignant(matricule);

            // Générer le PDF
            byte[] pdfBytes = pdfService.generateRecapitulatifHorairePdf(enseignant, cours);

            // Configurer les en-têtes HTTP pour l'affichage dans le navigateur
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("recap_horaire_" + enseignant.getMatricule() + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IDNotExist e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}