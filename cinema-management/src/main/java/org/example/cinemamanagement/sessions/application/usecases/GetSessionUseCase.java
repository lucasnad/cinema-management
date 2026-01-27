package org.example.cinemamanagement.sessions.application.usecases;

import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.domain.Session;

import java.util.Optional;
import java.util.UUID;

public class GetSessionUseCase {
    private final SessionRepositoryPort sessionRepositoryPort;

    public GetSessionUseCase(SessionRepositoryPort sessionRepositoryPort) {
        this.sessionRepositoryPort = sessionRepositoryPort;
    }

    public Optional<Session> execute(UUID id) {
        return sessionRepositoryPort.findById(id);
    }

}
