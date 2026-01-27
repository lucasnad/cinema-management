package org.example.cinemamanagement.sessions.adapters.outbound.persistence;

import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.domain.Session;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionRepositoryJpaAdapter implements SessionRepositoryPort {

    private final SpringDataSessionRepository repository;

    public SessionRepositoryJpaAdapter(SpringDataSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Session save(Session session) {
        SessionJpaEntity entity = new SessionJpaEntity(
                session.getId(),
                session.getMovieId(),
                session.getRoom(),
                session.getStartsAt()
        );

        repository.save(entity);
        return session;
    }

    @Override
    public boolean existsById(UUID sessionId) {
        return repository.existsById(sessionId);
    }

    @Override
    public List<Session> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> new Session(e.getId(), e.getMovieId(), e.getRoom(), e.getStartsAt()))
                .toList();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return repository.findById(id)
                .map(e -> new Session(e.getId(), e.getMovieId(), e.getRoom(), e.getStartsAt()));
    }
}
