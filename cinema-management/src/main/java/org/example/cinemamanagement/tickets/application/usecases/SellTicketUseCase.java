package org.example.cinemamanagement.tickets.application.usecases;

import org.example.cinemamanagement.tickets.application.ports.SessionQueryPort;
import org.example.cinemamanagement.tickets.application.ports.TicketRepositoryPort;
import org.example.cinemamanagement.tickets.domain.Ticket;

import java.time.LocalDateTime;
import java.util.UUID;

public class SellTicketUseCase {

    private final TicketRepositoryPort ticketRepository;
    private final SessionQueryPort sessionQuery;

    public SellTicketUseCase(TicketRepositoryPort ticketRepository,
                             SessionQueryPort sessionQuery) {
        this.ticketRepository = ticketRepository;
        this.sessionQuery = sessionQuery;
    }

    public Ticket execute(UUID sessionId, String seat, String customerName) {

        var session = sessionQuery.getSession(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        if (session.startsAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Não é possível vender ingresso para sessão já iniciada");
        }

        if (ticketRepository.existsBySessionIdAndSeat(sessionId, seat)) {
            throw new IllegalStateException("Assento já está ocupado para essa sessão");
        }

        Ticket ticket = Ticket.newTicket(sessionId, seat, customerName);
        return ticketRepository.save(ticket);
    }
}
