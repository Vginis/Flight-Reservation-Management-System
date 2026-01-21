package org.acme.util;

import org.acme.exception.ResourceNotFoundException;
import org.acme.representation.wssession.WsSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class WebSocketSessionStoreTest {

    @InjectMocks
    WebSocketSessionStore webSocketSessionStore;


    @Test
    void createSession_shouldStoreSessionAndReturnSessionId() {
        String userId = "user-1";
        String flightUUID = "flight-123";

        String sessionId = webSocketSessionStore.createSession(userId, flightUUID);
        Assertions.assertNotNull(sessionId);

        WsSession session = webSocketSessionStore.validate(sessionId, flightUUID);
        Assertions.assertEquals(userId, session.userId());
        Assertions.assertEquals(flightUUID, session.flightUUID());
        Assertions.assertTrue(session.expiresAt().isAfter(Instant.now()));
    }

    @Test
    void validate_shouldThrowException_whenSessionDoesNotExist() {
        String unknownSessionId = "unknown";
        String flightUUID = "flight-123";

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> webSocketSessionStore.validate(unknownSessionId, flightUUID)
        );
    }

    @Test
    void validate_shouldThrowException_whenFlightUUIDDoesNotMatch() {
        String sessionId = webSocketSessionStore.createSession("user-1", "flight-123");

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> webSocketSessionStore.validate(sessionId, "different-flight")
        );
    }

    @Test
    void validate_shouldThrowException_andRemoveSession_whenExpired() throws Exception {
        String sessionId = webSocketSessionStore.createSession("user-1", "flight-123");

        Map<String, WsSession> sessionsMap = webSocketSessionStore.getSessionsMap();
        WsSession expired = new WsSession(
                "user-1",
                "flight-123",
                Instant.now().minusSeconds(10)
        );
        sessionsMap.put(sessionId, expired);
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> webSocketSessionStore.validate(sessionId, "flight-123")
        );
        Assertions.assertFalse(sessionsMap.containsKey(sessionId));
    }

    @Test
    void remove_shouldDeleteSession() {
        String sessionId = webSocketSessionStore.createSession("user-1", "flight-123");
        webSocketSessionStore.remove(sessionId);

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> webSocketSessionStore.validate(sessionId, "flight-123")
        );
    }

}
