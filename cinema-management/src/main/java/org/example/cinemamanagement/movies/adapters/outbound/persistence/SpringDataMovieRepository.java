package org.example.cinemamanagement.movies.adapters.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataMovieRepository extends JpaRepository<MovieJpaEntity, UUID> {}
