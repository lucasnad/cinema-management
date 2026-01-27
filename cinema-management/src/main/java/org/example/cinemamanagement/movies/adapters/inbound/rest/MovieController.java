package org.example.cinemamanagement.movies.adapters.inbound.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.example.cinemamanagement.movies.application.usecases.CreateMovieUseCase;
import org.example.cinemamanagement.movies.application.usecases.GetMovieUseCase;
import org.example.cinemamanagement.movies.application.usecases.ListMoviesUseCase;
import org.example.cinemamanagement.movies.domain.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final CreateMovieUseCase createUseCase;
    private final GetMovieUseCase getUseCase;
    private final ListMoviesUseCase listUseCase;

    public MovieController(CreateMovieUseCase createUseCase, GetMovieUseCase getUseCase, ListMoviesUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    public Movie create(@Valid @RequestBody CreateMovieRequest request) {
        return createUseCase.execute(request.title(), request.durationInMinutes());
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable UUID id) {
        return getUseCase.execute(id)
                .orElseThrow(() -> new IllegalArgumentException("Filme não encontrado"));
    }

    @GetMapping
    public List<Movie> list() {
        return listUseCase.execute();
    }

    record CreateMovieRequest(
            @NotBlank String title,
            @Min(1) int durationInMinutes
    ) {}
}
