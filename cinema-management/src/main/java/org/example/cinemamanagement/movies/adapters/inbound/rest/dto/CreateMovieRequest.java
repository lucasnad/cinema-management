package org.example.cinemamanagement.movies.adapters.inbound.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateMovieRequest(
            @NotBlank String title,
            @Min(1) int durationInMinutes
    ) {}