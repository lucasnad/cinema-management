package org.example.cinemamanagement.tickets.adapters.inbound.rest;

import jakarta.validation.Valid;
import org.example.cinemamanagement.tickets.adapters.inbound.rest.dto.SellTicketRequest;
import org.example.cinemamanagement.tickets.application.usecases.ListTicketsUseCase;
import org.example.cinemamanagement.tickets.application.usecases.SellTicketUseCase;
import org.example.cinemamanagement.tickets.domain.Ticket;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final SellTicketUseCase sellTicketUseCase;
    private final ListTicketsUseCase listTicketsUseCase;

    public TicketController(SellTicketUseCase sellTicketUseCase,
                            ListTicketsUseCase listTicketsUseCase) {
        this.listTicketsUseCase = listTicketsUseCase;
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

    @GetMapping
    public List<Ticket> list() {
        return listTicketsUseCase.execute();
    }
}
