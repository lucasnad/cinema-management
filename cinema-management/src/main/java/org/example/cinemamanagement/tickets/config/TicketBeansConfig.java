package org.example.cinemamanagement.tickets.config;

import org.example.cinemamanagement.tickets.application.ports.SessionQueryPort;
import org.example.cinemamanagement.tickets.application.ports.TicketRepositoryPort;
import org.example.cinemamanagement.tickets.application.usecases.ListTicketsUseCase;
import org.example.cinemamanagement.tickets.application.usecases.SellTicketUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketBeansConfig {

    @Bean
    public SellTicketUseCase sellTicketUseCase(
            TicketRepositoryPort ticketRepository,
            SessionQueryPort sessionQueryPort
    ) {
        return new SellTicketUseCase(ticketRepository, sessionQueryPort);
    }

    @Bean
    public ListTicketsUseCase listTicketsUseCase(TicketRepositoryPort ticketRepository) {
        return new ListTicketsUseCase(ticketRepository);
    }
}
