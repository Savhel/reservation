package org.polytech.reservation.service.materiel.delete;

import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.service.interfaces.Deletable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteMaterielPedagogique implements Deletable<MaterielPedagogique> {

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;


    @Override
    public boolean delete(UUID idMateriel) {
        materielPedagogiqueRepository.deleteById(idMateriel);
        return false;
    }
}
