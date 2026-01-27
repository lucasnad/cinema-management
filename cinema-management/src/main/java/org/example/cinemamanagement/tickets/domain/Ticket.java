package org.example.cinemamanagement.tickets.domain;

import java.util.UUID;

public class Ticket {

    private final UUID id;
    private final UUID sessionId;
    private final String seat;
    private final String customerName;

    private Ticket(UUID id, UUID sessionId, String seat, String customerName) {
        if (sessionId == null) {
            throw new IllegalArgumentException("Sessão é obrigatória");
        }
        if (seat == null || seat.isBlank()) {
            throw new IllegalArgumentException("Assento é obrigatório");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }

        this.id = id;
        this.sessionId = sessionId;
        this.seat = seat;
        this.customerName = customerName;
    }

    public static Ticket newTicket(UUID sessionId, String seat, String customerName) {
        return new Ticket(UUID.randomUUID(), sessionId, seat, customerName);
    }

    public UUID getId() { return id; }
    public UUID getSessionId() { return sessionId; }
    public String getSeat() { return seat; }
    public String getCustomerName() { return customerName; }
}
