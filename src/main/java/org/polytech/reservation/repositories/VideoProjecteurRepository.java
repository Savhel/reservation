package org.polytech.reservation.repositories;

import org.polytech.reservation.models.VideoProjecteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoProjecteurRepository extends JpaRepository<VideoProjecteur, UUID> {
}
