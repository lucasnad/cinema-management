package org.example.cinemamanagement.sessions.application.ports;
import org.example.cinemamanagement.sessions.domain.Session;

import java.util.UUID;

public interface SessionRepositoryPort {

    Session save(Session session);

    boolean existsById(UUID sessionId);
}
