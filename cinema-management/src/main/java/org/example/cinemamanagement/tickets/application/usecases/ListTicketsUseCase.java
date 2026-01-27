package org.example.cinemamanagement.tickets.application.usecases;

import org.example.cinemamanagement.tickets.application.ports.TicketRepositoryPort;
import org.example.cinemamanagement.tickets.domain.Ticket;

import java.util.List;

public class ListTicketsUseCase {

    private final TicketRepositoryPort ticketRepositoryPort;

    public ListTicketsUseCase(TicketRepositoryPort ticketRepositoryPort) {
        this.ticketRepositoryPort = ticketRepositoryPort;
    }

    public List<Ticket> execute() {
        return ticketRepositoryPort.findAll();
    }
}
