package org.polytech.reservation.service.DTO.classes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.polytech.reservation.models.Cours;
import org.polytech.reservation.models.Formation;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnseignantDTO {

    @NonNull
    private String matricule;

    @NonNull
    private String motDePasse;

    @NonNull
    private String nom;

    @NonNull
    private String prenom;

    @NonNull
    private String mail;

    @NonNull
    private String tel;

}
