package org.acme.resource.websocket;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.acme.constant.ErrorMessages;
import org.acme.exception.InvalidRequestException;
import org.acme.representation.reservation.FlightSeatLayoutUpdateRepresentation;
import org.acme.representation.wssession.WsSession;
import org.acme.service.FlightService;
import org.acme.util.WebSocketSessionStore;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/flight-seat-layout/{flightUUID}")
@Blocking
@ApplicationScoped
public class FlightSeatLayoutWebSocket {

    private static final Jsonb JSONB = JsonbBuilder.create();
    private static final String FLIGHT_UUID_LABEL = "flightUUID";
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @Inject
    FlightService flightService;
    @Inject
    ManagedExecutor executor;
    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    WebSocketSessionStore webSocketSessionStore;

    @OnOpen
    public void onOpen(Session session, @PathParam(FLIGHT_UUID_LABEL) String flightUUID) {

        String sessionId = extractQueryParam(session);
        WsSession wsSession = webSocketSessionStore.validate(sessionId, flightUUID);

        if (wsSession == null) {
            close(session);
            return;
        }

        session.getUserProperties().put("userId", wsSession.userId());
        session.getUserProperties().put("sessionId", sessionId);
        session.getUserProperties().put(FLIGHT_UUID_LABEL, flightUUID);

        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    private String extractQueryParam(Session session) {
        String query = session.getQueryString();
        if (query == null) return null;

        return Arrays.stream(query.split("&"))
                .map(p -> p.split("="))
                .filter(p -> p.length == 2 && p[0].equals("sessionId"))
                .map(p -> p[1])
                .findFirst()
                .orElse(null);
    }

    private void close(Session session) {
        try {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "Unauthorized"
            ));
        } catch (IOException ignored) {
            //Intentionally left blank
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        executor.execute(() -> handleSeatUpdate(message, session));
    }

    private void handleSeatUpdate(String message, Session session) {
        try {
            FlightSeatLayoutUpdateRepresentation flightSeatMapUpdateRepresentation = JSONB.fromJson(message, FlightSeatLayoutUpdateRepresentation.class);
            String flightUUID = (String) session.getUserProperties().get(FLIGHT_UUID_LABEL);
            String username = (String) session.getUserProperties().get("userId");

            flightService.updateSeatState(flightSeatMapUpdateRepresentation, username);

            synchronized (sessions) {
                for(Session s: sessions) {
                    if(flightUUID.equals(s.getUserProperties().get(FLIGHT_UUID_LABEL))){
                        s.getBasicRemote().sendText(flightUUID);
                    }
                }
            }
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }
    }
}
