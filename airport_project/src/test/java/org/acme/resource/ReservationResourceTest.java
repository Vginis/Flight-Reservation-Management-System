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
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertEquals(2, reservations.size());
    }

    @Test
    public void findReservationByPassengerId() throws JsonMappingException, JsonProcessingException {
        List<ReservationRepresentation> reservations = given().queryParam("passengerId", 6)
                .when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(2, reservations.size());
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
        ReservationRepresentation savedArticle = given()
                .contentType(ContentType.JSON)
                .body(reservationRepresentation)
                .when()
                .post(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then().statusCode(201)
                .extract().as(ReservationRepresentation.class);
        //assertEquals(???, savedArticle.passenger);
        //assertEquals(1, savedArticle.outgoingFlights.size());
        //assertEquals(1, savedArticle.ingoingFlights.size());
        assertEquals(1, savedArticle.ticketList.size());
        assertEquals(240L, savedArticle.totalPrice);
    }

}
