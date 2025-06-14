package org.polytech.reservation.service;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.exceptions.SuppressionImpossible;
import org.polytech.reservation.models.Etudiant;
import org.polytech.reservation.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;

    @Autowired
    public EtudiantService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    // Créer un étudiant
    public Etudiant creerEtudiant(String nom, String prenom, String motDePasse, 
                                 LocalDate dateNaiss, String lieuNaiss, String mail,
                                 String niveau, String filiere) {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setMotDePasse(motDePasse);
        etudiant.setDateNaiss(dateNaiss);
        etudiant.setLieuNaiss(lieuNaiss);
        etudiant.setMail(mail);
        etudiant.setNiveau(niveau);
        etudiant.setFiliere(filiere);
        
        return etudiantRepository.save(etudiant);
    }

    // Récupérer tous les étudiants
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    // Récupérer un étudiant par son matricule
    public Etudiant getEtudiantByMatricule(String matricule) throws IDNotExist {
        return etudiantRepository.findById(matricule)
                .orElseThrow(() -> new IDNotExist("Étudiant non trouvé"));
    }

    // Récupérer des étudiants par nom
    public List<Etudiant> getEtudiantsByNom(String nom) {
        return etudiantRepository.findByNomContainingIgnoreCase(nom);
    }

    // Récupérer des étudiants par prénom
    public List<Etudiant> getEtudiantsByPrenom(String prenom) {
        return etudiantRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    // Récupérer des étudiants par niveau
    public List<Etudiant> getEtudiantsByNiveau(String niveau) {
        return etudiantRepository.findByNiveauContainingIgnoreCase(niveau);
    }

    // Récupérer des étudiants par filière
    public List<Etudiant> getEtudiantsByFiliere(String filiere) {
        return etudiantRepository.findByFiliereContainingIgnoreCase(filiere);
    }

    // Récupérer des étudiants par nom et prénom
    public List<Etudiant> getEtudiantsByNomAndPrenom(String nom, String prenom) {
        return etudiantRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(nom, prenom);
    }

    // Récupérer des étudiants par niveau et filière
    public List<Etudiant> getEtudiantsByNiveauAndFiliere(String niveau, String filiere) {
        return etudiantRepository.findByNiveauContainingIgnoreCaseAndFiliereContainingIgnoreCase(niveau, filiere);
    }

    // Modifier un étudiant
    public Etudiant modifierEtudiant(String matricule, String nom, String prenom, String motDePasse, 
                                    LocalDate dateNaiss, String lieuNaiss, String mail,
                                    String niveau, String filiere) throws IDNotExist, ModificationImpossible {
        Etudiant etudiant = etudiantRepository.findById(matricule)
                .orElseThrow(() -> new IDNotExist("Étudiant non trouvé"));

        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setMotDePasse(motDePasse);
        etudiant.setDateNaiss(dateNaiss);
        etudiant.setLieuNaiss(lieuNaiss);
        etudiant.setMail(mail);
        etudiant.setNiveau(niveau);
        etudiant.setFiliere(filiere);

        return etudiantRepository.save(etudiant);
    }

    // Supprimer un étudiant
    public void supprimerEtudiant(String matricule) throws IDNotExist {
        if (!etudiantRepository.existsById(matricule)) {
            throw new IDNotExist("Étudiant non trouvé");
        }
        etudiantRepository.deleteById(matricule);
    }
}