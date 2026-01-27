package org.example.cinemamanagement.sessions.config;
import org.example.cinemamanagement.sessions.application.ports.MovieQueryPort;
import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.application.usecases.CreateSessionUseCase;
import org.example.cinemamanagement.sessions.application.usecases.ListSessionsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionBeansConfig {

    @Bean
    public CreateSessionUseCase createSessionUseCase(
            SessionRepositoryPort sessionRepositoryPort, MovieQueryPort movieQueryPort
    ) {
        return new CreateSessionUseCase(sessionRepositoryPort, movieQueryPort);
    }

    @Bean
    public ListSessionsUseCase listSessionsUseCase(
            SessionRepositoryPort sessionRepositoryPort
    ) {
        return new ListSessionsUseCase(sessionRepositoryPort);
    }
}
