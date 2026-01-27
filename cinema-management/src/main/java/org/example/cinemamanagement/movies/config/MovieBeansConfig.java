package org.example.cinemamanagement.movies.config;

import org.example.cinemamanagement.movies.application.ports.MovieRepositoryPort;
import org.example.cinemamanagement.movies.application.usecases.CreateMovieUseCase;
import org.example.cinemamanagement.movies.application.usecases.GetMovieUseCase;
import org.example.cinemamanagement.movies.application.usecases.ListMoviesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieBeansConfig {

    @Bean
    public CreateMovieUseCase createMovieUseCase(MovieRepositoryPort repository) {
        return new CreateMovieUseCase(repository);
    }

    @Bean
    public GetMovieUseCase getMovieUseCase(MovieRepositoryPort repository) {
        return new GetMovieUseCase(repository);
    }

    @Bean
    public ListMoviesUseCase listMoviesUseCase(MovieRepositoryPort repository) {
        return new ListMoviesUseCase(repository);
    }

}
