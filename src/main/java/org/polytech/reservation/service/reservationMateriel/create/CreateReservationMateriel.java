package org.polytech.reservation.service.reservationMateriel.create;

import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.repositories.ReservationMaterielRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateReservationMateriel implements Creatable<ReservationMateriel> {

    @Autowired
    private ReservationMaterielRepository reservationMaterielRepository;

    @Override
    public ReservationMateriel create(ReservationMateriel reservationMateriel) {
        return reservationMaterielRepository.save(reservationMateriel);
    }
}
