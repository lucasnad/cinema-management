package org.example.cinemamanagement.sessions.application.usecases;

import org.example.cinemamanagement.sessions.application.ports.MovieQueryPort;
import org.example.cinemamanagement.sessions.application.ports.SessionRepositoryPort;
import org.example.cinemamanagement.sessions.domain.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateSessionUseCaseTest {

    @Test
    void deveCriarSessaoQuandoFilmeExiste() {
        // simula persistencia: retorna o próprio objeto salvo
        SessionRepositoryPort repositoryFake = Mockito.mock(SessionRepositoryPort.class);
        Mockito.when(repositoryFake.save(Mockito.any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // filme sempre existe
        MovieQueryPort movieQueryFake = _ -> true;

        CreateSessionUseCase useCase = new CreateSessionUseCase(repositoryFake, movieQueryFake);

        UUID movieId = UUID.randomUUID();
        Session session = useCase.execute(movieId, "Sala 1", LocalDateTime.now().plusDays(1));

        assertNotNull(session.getId());
        assertEquals(movieId, session.getMovieId());
        assertEquals("Sala 1", session.getRoom());
    }

    @Test
    void deveLancarErroQuandoFilmeNaoExiste() {
        SessionRepositoryPort repositoryFake = Mockito.mock(SessionRepositoryPort.class);
        Mockito.when(repositoryFake.save(Mockito.any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // filme nao existe
        MovieQueryPort movieQueryFake = _ -> false;

        CreateSessionUseCase useCase = new CreateSessionUseCase(repositoryFake, movieQueryFake);

        UUID movieId = UUID.randomUUID();

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(movieId, "Sala 1", LocalDateTime.now().plusDays(1))
        );

        assertEquals("Filme não encontrado", ex.getMessage());
    }
}
