package org.example.cinemamanagement.tickets.adapters.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketSpringDataRepository extends JpaRepository<TicketJpaEntity, UUID> {

    boolean existsBySessionIdAndSeat(UUID sessionId, String seat);
}
