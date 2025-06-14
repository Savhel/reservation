package org.polytech.reservation.service.DTO.classes;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalleDTO {

    @NonNull
    private UUID numSalle;

    @NonNull
    private String nomSalle;

    @NonNull
    private int nbPlace;
}
