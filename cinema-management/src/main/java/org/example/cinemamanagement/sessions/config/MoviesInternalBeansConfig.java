package org.example.cinemamanagement.sessions.config;

import org.example.cinemamanagement.movies.application.usecases.GetMovieUseCase;
import org.example.cinemamanagement.sessions.application.ports.MovieQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoviesInternalBeansConfig {

    @Bean
    MovieQueryPort movieQueryPort(GetMovieUseCase getMovieUseCase) {
        return movieId -> getMovieUseCase.execute(movieId).isPresent();
    }
}