package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Enseignant;
import org.polytech.reservation.models.ReservationSalle;
import org.polytech.reservation.models.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationSalleRepository extends JpaRepository<ReservationSalle, UUID> {
    
    List<ReservationSalle> findBySalle(Salle salle);
    
    List<ReservationSalle> findByEnseignant(Enseignant enseignant);
    
    List<ReservationSalle> findByCours(Cours cours);
    
    List<ReservationSalle> findByDateReservation(LocalDate dateReservation);
    
    @Query("SELECT r FROM ReservationSalle r WHERE r.salle = ?1 AND r.dateReservation = ?2")
    List<ReservationSalle> findBySalleAndDate(Salle salle, LocalDate dateReservation);
    
    @Query("SELECT r FROM ReservationSalle r WHERE r.enseignant = ?1 AND r.dateReservation = ?2")
    List<ReservationSalle> findByEnseignantAndDate(Enseignant enseignant, LocalDate dateReservation);
    
    @Query("SELECT r FROM ReservationSalle r WHERE r.dateReservation BETWEEN ?1 AND ?2")
    List<ReservationSalle> findByDateRange(LocalDate dateDebut, LocalDate dateFin);
}