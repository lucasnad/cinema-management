package org.example.cinemamanagement.movies.domain;

import java.util.UUID;

public class Movie {

    private final UUID id;
    private final String title;
    private final int durationInMinutes;

    public Movie(UUID id, String title, int durationInMinutes) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título do filme não pode ser vazio");
        }
        if (durationInMinutes <= 0) {
            throw new IllegalArgumentException("Duração do filme deve ser maior que zero");
        }

        this.id = id;
        this.title = title;
        this.durationInMinutes = durationInMinutes;
    }

    public static Movie newMovie(String title, int durationInMinutes) {
        return new Movie(UUID.randomUUID(), title, durationInMinutes);
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationInMinutes() { return durationInMinutes; }
}
