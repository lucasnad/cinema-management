package org.example.cinemamanagement.tickets.application.ports;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SessionQueryPort {

    Optional<SessionInfo> getSession(UUID sessionId);

    record SessionInfo(UUID id, LocalDateTime startsAt) {}
}
