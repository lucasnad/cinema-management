package org.example.cinemamanagement.sessions.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private final UUID id;
    private final UUID movieId;
    private final String room;
    private final LocalDateTime startsAt;

    public Session(UUID id, UUID movieId, String room, LocalDateTime startsAt) {
        if (movieId == null) {
            throw new IllegalArgumentException("Seção deve ter um 'movieId' válido");
        }
        if (room == null || room.isBlank()) {
            throw new IllegalArgumentException("Campo 'room' não pode ser nulo ou vazio");
        }
        if (startsAt == null) {
            throw new IllegalArgumentException("Campo 'startsAt' não pode ser nulo");
        }

        this.id = id;
        this.movieId = movieId;
        this.room = room;
        this.startsAt = startsAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public String getRoom() {
        return room;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public boolean hasStarted(LocalDateTime now) {
        return !now.isBefore(startsAt);
    }
}
