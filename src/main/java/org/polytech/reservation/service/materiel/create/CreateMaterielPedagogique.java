package org.polytech.reservation.service.materiel.create;

import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.service.interfaces.Creatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateMaterielPedagogique implements Creatable<MaterielPedagogique> {

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;

    @Override
    public MaterielPedagogique create(MaterielPedagogique materielPedagogique) {
        return materielPedagogiqueRepository.save(materielPedagogique);
    }
}
