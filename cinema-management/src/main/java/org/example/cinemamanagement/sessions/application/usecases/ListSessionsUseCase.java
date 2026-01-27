package org.example.cinemamanagement.sessions.application.usecases;
import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.domain.Session;

import java.util.List;

public class ListSessionsUseCase {

    private final SessionRepositoryPort repository;

    public ListSessionsUseCase(SessionRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Session> execute() {
        return repository.findAll();
    }
}
