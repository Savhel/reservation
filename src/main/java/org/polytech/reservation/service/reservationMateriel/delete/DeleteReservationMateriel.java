package org.polytech.reservation.service.reservationMateriel.delete;

import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.repositories.ReservationMaterielRepository;
import org.polytech.reservation.service.interfaces.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteReservationMateriel implements Deletable<ReservationMateriel> {

    @Autowired
    private ReservationMaterielRepository reservationMaterielRepository;


    @Override
    public boolean delete(UUID idMateriel) {
        reservationMaterielRepository.deleteById(idMateriel);
        return false;
    }
}
