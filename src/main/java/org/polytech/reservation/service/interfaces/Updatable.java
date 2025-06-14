package org.polytech.reservation.service.interfaces;

import java.util.UUID;

public interface Updatable <T>{
    T update(T t, UUID id);

}
