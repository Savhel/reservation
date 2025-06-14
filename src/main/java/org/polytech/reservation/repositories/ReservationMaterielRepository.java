package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.models.ReservationMateriel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationMaterielRepository extends JpaRepository<ReservationMateriel, UUID> {

    // Méthodes existantes
    List<ReservationMateriel> getReservationMaterielByMateriel(MaterielPedagogique materiel);
    List<ReservationMateriel> getReservationMaterielByCours(Cours cours);
    
    // Nouvelles méthodes
    // Trouver les réservations par matériel
    List<ReservationMateriel> findByMaterielId(UUID idMateriel);
    
    // Trouver les réservations par enseignant
    List<ReservationMateriel> findByEnseignantMatricule(String matricule);
    
    // Trouver les réservations par cours
    List<ReservationMateriel> findByCoursId(UUID idCours);
    
    // Trouver les réservations par date
    List<ReservationMateriel> findByDateReservation(LocalDate date);
    
    // Trouver les réservations par matériel et date
    List<ReservationMateriel> findByMaterielIdAndDateReservation(UUID idMateriel, LocalDate date);
    
    // Trouver les réservations par enseignant et date
    List<ReservationMateriel> findByEnseignantMatriculeAndDateReservation(String matricule, LocalDate date);
    
    // Trouver les réservations par plage de dates
    List<ReservationMateriel> findByDateReservationBetween(LocalDate dateDebut, LocalDate dateFin);
}
