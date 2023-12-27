package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.acme.persistence.JPATest;
import org.acme.representation.FlightRepresentation;
import org.acme.representation.ReservationRepresentation;
import org.acme.util.Fixture;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import io.quarkus.test.TestTransaction;

import java.util.List;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ReservationResourceTest extends JPATest {

    @Test
    public void findAllReservations() {
        List<ReservationRepresentation> reservations = when().get(Fixture.API_ROOT+AirportProjectURIs.RESERVATIONS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(3, reservations.size());
    }

    @Test
    public void findReservationByPassengerId() throws JsonMappingException, JsonProcessingException {
        List<ReservationRepresentation> reservations = given().queryParam("passengerId", 6)
                .when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(3, reservations.size());
    }

    @Test
    public void findExistingReservation() {
        ReservationRepresentation a = when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + Fixture.Reservations.RESERVATION_ONE_WAY_ID)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);
        assertEquals(Fixture.Reservations.RESERVATION_ONE_WAY_ID, a.reservationId);
    }

    @Test
    public void findNoExistingReservation() {
        when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 32)
                .then()
                .statusCode(404);
    }

    // TODO δεν πρόλαβα
    @Test
    public void createReservation() {
        ReservationRepresentation reservationRepresentation = Fixture.getReservationRepresentation();
        ReservationRepresentation savedReservation = given()
                .contentType(ContentType.JSON)
                .body(reservationRepresentation)
                .when()
                .post(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(201)
                .extract().as(ReservationRepresentation.class);
        assertEquals(1, savedReservation.outgoingFlights.size());
        assertEquals("A3651", savedReservation.outgoingFlights.get(0));
        assertEquals(1, savedReservation.ingoingFlights.size());
        assertEquals(1, savedReservation.ticketList.size());
        assertEquals(240L, savedReservation.totalPrice);
    }

    @Test
    public void updateReservation() {
        ReservationRepresentation reservation = when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);

        reservation.outgoingFlights = new ArrayList<>();
        reservation.ingoingFlights = new ArrayList<>();
        reservation.ticketList = new ArrayList<>();
        reservation.totalPrice = (long) 240;

        given().contentType(ContentType.JSON).body(reservation)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then().statusCode(204);

        ReservationRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);

        assertEquals(240, updated.totalPrice);
    }

    @Test
    @TestTransaction
    public void removeExistingReservation(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 11)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }


}
