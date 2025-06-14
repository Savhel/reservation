package org.polytech.reservation.service.interfaces;

import java.util.UUID;

public interface Deletable <T>{
    boolean delete(UUID id);
}
