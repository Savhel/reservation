package org.polytech.reservation.service.reservationMateriel.read;

import org.polytech.reservation.exceptions.IDNotExist;
import org.polytech.reservation.service.interfaces.Readable;
import org.polytech.reservation.models.ReservationMateriel;
import org.polytech.reservation.repositories.CoursRepository;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.repositories.ReservationMaterielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadReservationMateriel implements Readable<ReservationMateriel> {

    @Autowired
    private ReservationMaterielRepository reservationMaterielRepository;

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;

    @Autowired
    private CoursRepository coursRepository;

    @Override
    public ReservationMateriel read(UUID idReservationMateriel) {
        return reservationMaterielRepository.findById(idReservationMateriel).orElse(null);
    }
    @Override
    public List<ReservationMateriel> readlist() {
        return reservationMaterielRepository.findAll();
    }

    public List<ReservationMateriel> listReservationMaterielMaterielPedagogique(UUID idMaterielPedagogique) {
        if (materielPedagogiqueRepository.findById(idMaterielPedagogique).isPresent()){
            return reservationMaterielRepository.getReservationMaterielByMateriel(materielPedagogiqueRepository.findById(idMaterielPedagogique).get());
        }
        throw new IDNotExist("Nous ne connaissons pas ce materiel pedagogique");
    }

    public List<ReservationMateriel> listReservationMaterielCours(UUID idCours) {
        if (coursRepository.findById(idCours).isPresent()){
            return reservationMaterielRepository.getReservationMaterielByCours(coursRepository.findById(idCours).get());
        }
        throw new IDNotExist("Nous ne connaissons pas ce cours");
    }
}
