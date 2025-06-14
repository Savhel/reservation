package org.polytech.reservation.service.materiel.read;

import org.polytech.reservation.service.interfaces.Readable;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadMaterielPedagogique implements Readable<MaterielPedagogique> {

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;


    @Override
    public MaterielPedagogique read(UUID idMaterielPedagogique) {
        return materielPedagogiqueRepository.findById(idMaterielPedagogique).orElse(null);
    }

    @Override
    public List<MaterielPedagogique> readlist() {
        return materielPedagogiqueRepository.findAll();
    }

}
