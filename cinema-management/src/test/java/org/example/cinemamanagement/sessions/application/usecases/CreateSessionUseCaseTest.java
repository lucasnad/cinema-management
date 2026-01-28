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

    /**
     * Caso de uso pra demonstrar como está fácil testar devido ao baixo acoplamento e abstrações.
     * Só precisa mockar as portas (interfaces) que são dependências do caso de uso.
     */
    @Test
    void deveCriarSessaoQuandoFilmeExiste() {
        // repoFake usando Mockito.
        SessionRepositoryPort repositoryFake = Mockito.mock(SessionRepositoryPort.class);

        // devolve ele mesmo
        Mockito.when(repositoryFake.save(Mockito.any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock que sempre retorna true pra indicar que o filme existe
        MovieQueryPort movieQueryFake = movieId -> true;

        // So mockei repo e porta e pronto
        CreateSessionUseCase useCase = new CreateSessionUseCase(repositoryFake, movieQueryFake);

        UUID movieId = UUID.randomUUID();
        Session session = useCase.execute(movieId, "Sala 1", LocalDateTime.now().plusDays(1));

        assertNotNull(session.getId());
        assertEquals(movieId, session.getMovieId());
        assertEquals("Sala 1", session.getRoom());
    }

    /**
     * Teste negativo, onde o filme não existe.
     */
    @Test
    void deveLancarErroQuandoFilmeNaoExiste() {
        SessionRepositoryPort repositoryFake = Mockito.mock(SessionRepositoryPort.class);
        Mockito.when(repositoryFake.save(Mockito.any(Session.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Filme não existe
        MovieQueryPort movieQueryFake = movieId -> false;

        CreateSessionUseCase useCase = new CreateSessionUseCase(repositoryFake, movieQueryFake);

        UUID movieId = UUID.randomUUID();

        Exception ex = assertThrows(IllegalStateException.class, () ->
                useCase.execute(movieId, "Sala 1", LocalDateTime.now().plusDays(1))
        );

        assertEquals("Filme não encontrado", ex.getMessage());
    }
}
