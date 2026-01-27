package org.example.cinemamanagement.movies.adapters.outbound.persistence;

import org.example.cinemamanagement.movies.application.ports.MovieRepositoryPort;
import org.example.cinemamanagement.movies.domain.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MovieRepositoryJpaAdapter implements MovieRepositoryPort {

    private final SpringDataMovieRepository repository;

    public MovieRepositoryJpaAdapter(SpringDataMovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movie save(Movie movie) {
        MovieJpaEntity entity = new MovieJpaEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationInMinutes()
        );
        repository.save(entity);
        return movie;
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return repository.findById(id)
                .map(e -> new Movie(e.getId(), e.getTitle(), e.getDurationInMinutes()));
    }

    @Override
    public List<Movie> findAll() {
        return repository.findAll()
                .stream()
                .map(e -> new Movie(
                        e.getId(),
                        e.getTitle(),
                        e.getDurationInMinutes()
                ))
                .toList();
    }

}