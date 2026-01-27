package org.example.cinemamanagement.tickets.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SellTicketRequest(
            @NotNull UUID sessionId,
            @NotBlank String seat,
            @NotBlank String customerName
    ) {}