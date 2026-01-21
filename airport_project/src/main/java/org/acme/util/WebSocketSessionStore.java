package org.acme.util;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constant.ErrorMessages;
import org.acme.exception.ResourceNotFoundException;
import org.acme.representation.wssession.WsSession;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class WebSocketSessionStore {

    private static final Duration TIME_TO_LIVE = Duration.ofMinutes(2);
    private final Map<String, WsSession> sessionsMap = new ConcurrentHashMap<>();

    public String createSession(String userId, String flightUUID) {
        String sessionId = UUID.randomUUID().toString();
        sessionsMap.put(sessionId, new WsSession(userId, flightUUID, Instant.now().plus(TIME_TO_LIVE)));
        return sessionId;
    }

    public WsSession validate(String sessionId, String flightUUID) {
        WsSession session = sessionsMap.get(sessionId);

        if (session == null || !session.flightUUID().equals(flightUUID)) {
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        if (session.expiresAt().isBefore(Instant.now())) {
            sessionsMap.remove(sessionId);
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return session;
    }

    public void remove(String sessionId) {
        sessionsMap.remove(sessionId);
    }

    public Map<String, WsSession> getSessionsMap() {
        return sessionsMap;
    }
}
