package org.example.cinemamanagement.tickets.domain;

import java.util.UUID;

public class Ticket {

    private final UUID id;
    private final UUID sessionId;
    private final String seat;
    private final String customerName;

    public Ticket(UUID id, UUID sessionId, String seat, String customerName) {
        if (sessionId == null) {
            throw new IllegalArgumentException("Ticket deve estar associado a uma sessão válida");
        }
        if (seat == null || seat.isBlank()) {
            throw new IllegalArgumentException("Campo 'seat' não pode ser nulo ou vazio");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Campo 'customerName' não pode ser nulo ou vazio");
        }

        this.id = id;
        this.sessionId = sessionId;
        this.seat = seat;
        this.customerName = customerName;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getSeat() {
        return seat;
    }

    public String getCustomerName() {
        return customerName;
    }
}
