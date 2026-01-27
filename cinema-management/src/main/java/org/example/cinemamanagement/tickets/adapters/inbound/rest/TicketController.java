package org.example.cinemamanagement.tickets.adapters.inbound.rest;

import jakarta.validation.Valid;
import org.example.cinemamanagement.tickets.adapters.inbound.rest.dto.SellTicketRequest;
import org.example.cinemamanagement.tickets.application.usecases.SellTicketUseCase;
import org.example.cinemamanagement.tickets.domain.Ticket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final SellTicketUseCase sellTicketUseCase;

    public TicketController(SellTicketUseCase sellTicketUseCase) {
        this.sellTicketUseCase = sellTicketUseCase;
    }

    @PostMapping
    public Ticket sell(@Valid @RequestBody SellTicketRequest request) {
        return sellTicketUseCase.execute(
                request.sessionId(),
                request.seat(),
                request.customerName()
        );
    }
}
