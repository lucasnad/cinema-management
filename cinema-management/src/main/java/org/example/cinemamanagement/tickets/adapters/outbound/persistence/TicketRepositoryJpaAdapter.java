package org.example.cinemamanagement.tickets.adapters.outbound.persistence;

import org.example.cinemamanagement.tickets.application.ports.TicketRepositoryPort;
import org.example.cinemamanagement.tickets.domain.Ticket;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TicketRepositoryJpaAdapter implements TicketRepositoryPort {

    private final TicketSpringDataRepository repository;

    public TicketRepositoryJpaAdapter(TicketSpringDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ticket save(Ticket ticket) {
        TicketJpaEntity entity = new TicketJpaEntity(
                ticket.getId(),
                ticket.getSessionId(),
                ticket.getSeat(),
                ticket.getCustomerName()
        );
        repository.save(entity);
        return ticket;
    }

    @Override
    public boolean existsBySessionIdAndSeat(UUID sessionId, String seat) {
        return repository.existsBySessionIdAndSeat(sessionId, seat);
    }
}
