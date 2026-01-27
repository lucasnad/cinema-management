package org.example.cinemamanagement.tickets.config;

import org.example.cinemamanagement.tickets.application.ports.SessionQueryPort;
import org.example.cinemamanagement.sessions.application.usecases.GetSessionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketsInternalBeansConfig {

    /**
     * Integração interna entre módulos (Monólito).
     *
     * GetSessionUseCase pertence ao módulo de Sessions.
     * Aqui implementamos a porta SessionQueryPort do módulo de Tickets
     * utilizando diretamente um caso de uso de Sessions.
     *
     * Dessa forma, Tickets não depende do módulo de Sessions, apenas da
     * interface (port). A ligação concreta entre os módulos acontece
     * na configuração do monólito.
     *
     * Em uma evolução para microserviços ou SOA, essa implementação
     * poderia ser substituída por um client HTTP sem alterar o módulo Tickets.
     */
    @Bean
    SessionQueryPort sessionQueryPort(GetSessionUseCase getSessionUseCase) {
        return sessionId -> getSessionUseCase.execute(sessionId)
                .map(s -> new SessionQueryPort.SessionInfo(s.getId(), s.getStartsAt()));
    }
}
