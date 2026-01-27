package org.example.cinemamanagement.movies.adapters.outbound.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "movies")
public class MovieJpaEntity {

    @Id
    private UUID id;
    private String title;
    private int durationInMinutes;

    protected MovieJpaEntity() {}

    public MovieJpaEntity(UUID id, String title, int durationInMinutes) {
        this.id = id;
        this.title = title;
        this.durationInMinutes = durationInMinutes;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationInMinutes() { return durationInMinutes; }
}
