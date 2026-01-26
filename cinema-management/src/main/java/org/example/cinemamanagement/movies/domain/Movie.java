package org.example.cinemamanagement.movies.domain;

import java.util.UUID;

public class Movie {
    private final UUID id;
    private final String title;
    private final int durationMinutes;

    public Movie(UUID id, String title, int durationMinutes) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser nulo ou vazio");
        }

        if(durationMinutes <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero");
        }

        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}
