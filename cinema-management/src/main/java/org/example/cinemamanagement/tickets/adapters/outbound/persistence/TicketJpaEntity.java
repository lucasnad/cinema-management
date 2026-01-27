package org.example.cinemamanagement.tickets.adapters.outbound.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tickets")
public class TicketJpaEntity {

    @Id
    private UUID id;
    private UUID sessionId;
    private String seat;
    private String customerName;

    protected TicketJpaEntity() {}

    public TicketJpaEntity(UUID id, UUID sessionId, String seat, String customerName) {
        this.id = id;
        this.sessionId = sessionId;
        this.seat = seat;
        this.customerName = customerName;
    }

    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public String getSeat() { return seat; }
    public String getCustomerName() { return customerName; }
}
