package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.reservation.ReservationRepresentation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ReservationResourceTest extends JPATest {

    static final String API_ROOT  = "http://localhost:8081";

    @Test
    void findAllReservations() {
        List<ReservationRepresentation> reservations = when().get(API_ROOT+ AirportProjectURIs.RESERVATIONS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(3, reservations.size());
    }

    @Test
    void findReservationByPassengerId() {
        List<ReservationRepresentation> reservations = given().queryParam("passengerId", 6)
                .when().get(API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(3, reservations.size());
    }

    @Test
    void findExistingReservation() {
        ReservationRepresentation a = when().get(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);
        assertEquals(9, a.reservationId);
    }

    @Test
    void findNoExistingReservation() {
        when().get(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 32)
                .then()
                .statusCode(404);
    }

    @Test
    void createReservation() {
        ReservationRepresentation reservationRepresentation = new ReservationRepresentation();
        ReservationRepresentation savedReservation = given()
                .contentType(ContentType.JSON)
                .body(reservationRepresentation)
                .when()
                .post(API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(201)
                .extract().as(ReservationRepresentation.class);
        assertEquals(1, savedReservation.outgoingFlights.size());
        assertEquals("A3651", savedReservation.outgoingFlights.get(0));
        assertEquals(1, savedReservation.ingoingFlights.size());
        assertEquals(1, savedReservation.ticketList.size());
        assertEquals(240L, savedReservation.totalPrice);
    }

    @Test
    void updateReservation() {
        ReservationRepresentation reservation = when().get(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);

        reservation.outgoingFlights = new ArrayList<>();
        reservation.ingoingFlights = new ArrayList<>();
        reservation.ticketList = new ArrayList<>();
        reservation.totalPrice = (long) 240;

        given().contentType(ContentType.JSON).body(reservation)
                .when().put(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then().statusCode(204);

        ReservationRepresentation updated = when().get(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);

        assertEquals(240, updated.totalPrice);
    }

    @Test
    @TestTransaction
    void removeExistingReservation(){
        when()
                .delete(API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + 11)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }


}
