package org.acme.resource.websocket;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.acme.domain.FlightSeatLayout;
import org.acme.representation.reservation.FlightSeatLayoutUpdateRepresentation;
import org.acme.service.FlightService;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/flight-seat-layout/{flightUUID}")
@Blocking
@ApplicationScoped
public class FlightSeatLayoutWebSocket {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static final Jsonb JSONB = JsonbBuilder.create();
    private static final String FLIGHT_UUID_LABEL = "flightUUID";

    @Inject
    FlightService flightService;
    @Inject
    ManagedExecutor executor;

    @OnOpen
    public void onOpen(Session session, @PathParam(FLIGHT_UUID_LABEL) String flightUUID) {
        session.getUserProperties().put(FLIGHT_UUID_LABEL, flightUUID);
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        executor.execute(() -> handleSeatUpdate(message, session));
    }

    private void handleSeatUpdate(String message, Session session) {
        try {
            FlightSeatLayoutUpdateRepresentation flightSeatMapUpdateRepresentation = JSONB.fromJson(message, FlightSeatLayoutUpdateRepresentation.class);
            FlightSeatLayout flightSeatLayout = flightService.updateSeatState(flightSeatMapUpdateRepresentation);
            //String responseJson = JSONB.toJson(flightSeatLayout);
            String layoutId = (String) session.getUserProperties().get(FLIGHT_UUID_LABEL);

            synchronized (sessions) {
                for(Session s: sessions) {
                    if(layoutId.equals(s.getUserProperties().get(FLIGHT_UUID_LABEL))){
                        s.getBasicRemote().sendText(layoutId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
