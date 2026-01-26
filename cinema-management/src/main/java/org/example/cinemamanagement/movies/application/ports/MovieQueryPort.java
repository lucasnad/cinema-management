package org.example.cinemamanagement.movies.application.ports;

import java.util.UUID;

public interface MovieQueryPort {

    boolean movieExists(UUID movieId);
}