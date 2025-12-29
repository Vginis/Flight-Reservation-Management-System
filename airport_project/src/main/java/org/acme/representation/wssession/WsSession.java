package org.acme.representation.wssession;

import java.time.Instant;

public record WsSession(
        String userId,
        String flightUUID,
        Instant expiresAt
) {}
