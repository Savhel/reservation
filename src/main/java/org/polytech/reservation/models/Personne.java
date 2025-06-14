package org.polytech.reservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_personne")
public abstract class Personne {

    @Id
    private String matricule;

    @NonNull
    @NotNull(message = "Le mot de passe est obligatoire.")
    @Column(nullable = false)
    private String motDePasse;

    @NonNull
    @Column(nullable = false)
    private String nom;

    @NonNull
    @Column(nullable = false)
    private String prenom;

    @NonNull
    @Column(nullable = false)
    private LocalDate dateNaiss;

    @NonNull
    @Column(nullable = false)
    private String lieuNaiss;

    @Email(message = "L'adresse e-mail doit être valide.")
    @NotBlank(message = "L'e-mail est obligatoire.")
    @Column(unique = true, nullable = false)
    private String mail;
}