package org.polytech.reservation.repositories;

import org.polytech.reservation.models.Ordinateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdinateurRepository extends JpaRepository<Ordinateur, UUID> {
}
