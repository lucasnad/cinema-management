package org.example.cinemamanagement.sessions.application.ports;

import java.util.UUID;

public interface MovieQueryPort {
    boolean movieExists(UUID movieId);
}
