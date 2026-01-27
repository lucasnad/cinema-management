package org.example.cinemamanagement.movies.application.usecases;

import org.example.cinemamanagement.movies.application.ports.MovieRepositoryPort;
import org.example.cinemamanagement.movies.domain.Movie;

import java.util.UUID;

public class CreateMovieUseCase {

    private final MovieRepositoryPort repository;

    public CreateMovieUseCase(MovieRepositoryPort repository) {
        this.repository = repository;
    }

    public Movie execute(String title, int durationInMinutes) {
        // Criando entidade pelo próprio domínio, como mostrado em aula. Garantindo que o objeto ja nasça válido.
        Movie movie = Movie.newMovie(title, durationInMinutes);
        return repository.save(movie);
    }
}
