package org.example.cinemamanagement.movies.application.ports;

import org.example.cinemamanagement.movies.domain.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepositoryPort {

    Movie save(Movie movie);

    Optional<Movie> findById(UUID id);

    List<Movie> findAll();
}
