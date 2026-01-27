package org.example.cinemamanagement.sessions.adapters.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataSessionRepository extends JpaRepository<SessionJpaEntity, UUID> {
}
