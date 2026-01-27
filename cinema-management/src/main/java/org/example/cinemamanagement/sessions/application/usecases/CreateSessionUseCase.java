package org.example.cinemamanagement.sessions.application.usecases;
import org.example.cinemamanagement.sessions.application.ports.MovieQueryPort;
import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.domain.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateSessionUseCase {

    private final SessionRepositoryPort sessionRepository;
    private final MovieQueryPort movieQueryPort;

    public CreateSessionUseCase(SessionRepositoryPort sessionRepository, MovieQueryPort movieQueryPort) {
        this.sessionRepository = sessionRepository;
        this.movieQueryPort = movieQueryPort;
    }

    public Session execute(UUID movieId, String room, LocalDateTime startsAt) {

        if (!movieQueryPort.movieExists(movieId)) {
            throw new IllegalArgumentException("Filme não encontrado");
        }

        Session session = new Session(UUID.randomUUID(), movieId, room, startsAt);

        return sessionRepository.save(session);
    }
}
