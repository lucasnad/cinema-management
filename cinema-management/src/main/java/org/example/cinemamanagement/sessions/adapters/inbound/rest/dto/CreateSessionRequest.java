package org.example.cinemamanagement.sessions.adapters.inbound.rest.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSessionRequest(@NotNull UUID movieId, @NotBlank String room, @NotNull @Future LocalDateTime startsAt) {}