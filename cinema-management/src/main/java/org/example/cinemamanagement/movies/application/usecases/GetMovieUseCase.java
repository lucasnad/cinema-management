package org.example.cinemamanagement.movies.application.usecases;

import org.example.cinemamanagement.movies.application.ports.MovieRepositoryPort;
import org.example.cinemamanagement.movies.domain.Movie;

import java.util.Optional;
import java.util.UUID;

public class GetMovieUseCase {

    private final MovieRepositoryPort repository;

    public GetMovieUseCase(MovieRepositoryPort repository) {
        this.repository = repository;
    }

    public Optional<Movie> execute(UUID movieId) {
        return repository.findById(movieId);
    }
}
