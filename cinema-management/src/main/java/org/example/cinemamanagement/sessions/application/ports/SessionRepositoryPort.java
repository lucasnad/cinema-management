package org.example.cinemamanagement.sessions.application.ports;
import org.example.cinemamanagement.sessions.domain.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepositoryPort {

    Session save(Session session);

    boolean existsById(UUID sessionId);

    Optional<Session> findById(UUID id);

    List<Session> findAll();

}
