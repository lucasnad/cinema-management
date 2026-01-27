package org.example.cinemamanagement.sessions.adapters.inbound.rest;

import jakarta.validation.Valid;
import org.example.cinemamanagement.sessions.adapters.inbound.rest.dto.CreateSessionRequest;
import org.example.cinemamanagement.sessions.application.usecases.CreateSessionUseCase;
import org.example.cinemamanagement.sessions.application.usecases.ListSessionsUseCase;
import org.example.cinemamanagement.sessions.domain.Session;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final CreateSessionUseCase createUseCase;
    private final ListSessionsUseCase listUseCase;

    public SessionController(CreateSessionUseCase createUseCase, ListSessionsUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    public Session create(@Valid @RequestBody CreateSessionRequest request) {
        return createUseCase.execute(
                request.movieId(),
                request.room(),
                request.startsAt()
        );
    }

    @GetMapping
    public List<Session> list() {
        return listUseCase.execute();
    }

}
