package org.example.cinemamanagement.tickets.application.ports;

import org.example.cinemamanagement.tickets.domain.Ticket;

import java.util.UUID;

public interface TicketRepositoryPort {

    Ticket save(Ticket ticket);

    boolean existsBySessionIdAndSeat(UUID sessionId, String seat);
}
