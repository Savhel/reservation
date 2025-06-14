package org.polytech.reservation.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface Readable <T>{

    T read(UUID id);
    List<T> readlist();
}
