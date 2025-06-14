package org.polytech.reservation.service.materiel.update;

import org.polytech.reservation.exceptions.ModificationImpossible;
import org.polytech.reservation.models.MaterielPedagogique;
import org.polytech.reservation.repositories.MaterielPedagogiqueRepository;
import org.polytech.reservation.service.interfaces.Updatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UpdateMaterielPedagogique implements Updatable<MaterielPedagogique> {

    @Autowired
    private MaterielPedagogiqueRepository materielPedagogiqueRepository;

    @Override
    public MaterielPedagogique update(MaterielPedagogique materielPedagogique, UUID idMaterielPedagogique) {
        Optional<MaterielPedagogique> oldMaterielPedagogique = materielPedagogiqueRepository.findById(idMaterielPedagogique);
        if(oldMaterielPedagogique.isPresent()){
            MaterielPedagogique newMaterielPedagogique = oldMaterielPedagogique.get();
            newMaterielPedagogique.setNom(materielPedagogique.getNom());
            newMaterielPedagogique.setMarque(oldMaterielPedagogique.get().getMarque());
            newMaterielPedagogique.setReservationMateriels(oldMaterielPedagogique.get().getReservationMateriels());
            return materielPedagogiqueRepository.save(newMaterielPedagogique);
        }
        throw new ModificationImpossible("Une erreur est survenue lors de la modification de vos informations");
    }
}
