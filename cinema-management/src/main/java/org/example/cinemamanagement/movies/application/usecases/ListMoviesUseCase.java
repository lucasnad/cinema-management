package org.example.cinemamanagement.movies.application.usecases;

import org.example.cinemamanagement.movies.application.ports.MovieRepositoryPort;
import org.example.cinemamanagement.movies.domain.Movie;

import java.util.List;

public class ListMoviesUseCase {

    private final MovieRepositoryPort repository;

    public ListMoviesUseCase(MovieRepositoryPort repository) {
        this.repository = repository;
    }

    public List<Movie> execute() {
        return repository.findAll();
    }
}
