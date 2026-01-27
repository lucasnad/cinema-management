package org.example.cinemamanagement.sessions.adapters.outbound.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class SessionJpaEntity {

    @Id
    private UUID id;
    private UUID movieId;
    private String room;
    private LocalDateTime startsAt;

    protected SessionJpaEntity() {
    }

    public SessionJpaEntity(UUID id, UUID movieId, String room, LocalDateTime startsAt) {
        this.id = id;
        this.movieId = movieId;
        this.room = room;
        this.startsAt = startsAt;
    }

    public UUID getId() { return id; }
    public UUID getMovieId() { return movieId; }
    public String getRoom() { return room; }
    public LocalDateTime getStartsAt() { return startsAt; }
}
