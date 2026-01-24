package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.FlightStatus;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.persistence.JPATest;
import org.acme.profile.IsolatedDbProfile;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.util.FlightUtil;
import org.acme.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@TestProfile(IsolatedDbProfile.class)
class FlightResourceTest extends JPATest {

    @InjectMock
    UserContext userContext;

    FlightCreateRepresentation flightCreateRepresentation;
    FlightDateUpdateRepresentation flightDateUpdateRepresentation;
    @BeforeEach
    void setup() {
        flightCreateRepresentation = FlightUtil.createFlightCreateRepresentation();
        flightCreateRepresentation.setDepartureAirport("FCO");
        flightCreateRepresentation.setDepartureTime(LocalDateTime.now().plusYears(1));
        flightCreateRepresentation.setArrivalTime(LocalDateTime.now().plusYears(1).plusHours(5));

        flightDateUpdateRepresentation = FlightUtil.createFlightDateUpdateRepresentation();
        flightDateUpdateRepresentation.setDepartureTime(LocalDateTime.now().plusYears(1));
        flightDateUpdateRepresentation.setArrivalTime(LocalDateTime.now().plusYears(1).plusHours(5));
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_searchFlights() {

        given()
            .when()
            .get(AirportProjectURIs.FLIGHTS)
            .then()
            .statusCode(200)
            .body("results.size()", greaterThanOrEqualTo(2));
    }

    @Test
    void test_searchFlights_returns_401() {
        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .when()
            .get(AirportProjectURIs.FLIGHTS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_getFlightLayoutByUUID() {
        given()
            .when()
            .get(AirportProjectURIs.FLIGHTS+"/flight-seat-layout/123e4567-e89b-12d3-a456-426614174000")
            .then()
            .statusCode(200);
    }

    @Test
    void test_getFlightLayoutByUUID_returns_401() {
        given()
            .when()
            .get(AirportProjectURIs.FLIGHTS+"/flight-seat-layout/123e4567-e89b-12d3-a456-426614174000")
            .then()
            .statusCode(401);
    }

    @Test
    void test_searchByMultipleParams() {
        given()
            .when()
            .get(AirportProjectURIs.FLIGHTS+"/multiple-params")
            .then()
            .statusCode(200)
            .body("results.size()", greaterThanOrEqualTo(2));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "airline_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void createFlight_success() {
        Mockito.when(userContext.extractUsername()).thenReturn("admin1");
        given()
            .contentType(ContentType.JSON)
            .body(flightCreateRepresentation)
            .when()
            .post(AirportProjectURIs.FLIGHTS)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.FLIGHT_CREATION_SUCCESS));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void createFlight_returns_403() {
        given()
            .contentType(ContentType.JSON)
            .body(flightCreateRepresentation)
            .when()
            .post(AirportProjectURIs.FLIGHTS)
            .then()
            .statusCode(403);
    }

    @Test
    @TestTransaction
    void createFlight_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(flightCreateRepresentation)
            .when()
            .post(AirportProjectURIs.FLIGHTS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "airline_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void updateFlightDates_success() {
        given()
            .contentType(ContentType.JSON)
            .body(flightCreateRepresentation)
            .when()
            .put(AirportProjectURIs.FLIGHTS+"/1")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.FLIGHT_UPDATE_SUCCESS));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void updateFlightDates_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(flightCreateRepresentation)
                .when()
                .put(AirportProjectURIs.FLIGHTS+"/1")
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    void updateFlightDates_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(flightCreateRepresentation)
                .when()
                .put(AirportProjectURIs.FLIGHTS+"/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "airline_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void cancelFlight_success() {
        given()
            .queryParam("newStatus", FlightStatus.CANCELLED.value())
            .when()
            .put(AirportProjectURIs.FLIGHTS+"/update-flight-status/1")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.FLIGHT_CANCEL_SUCCESS));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "airline_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void cancelFlight_returns_400_invalid_status() {
        given()
            .queryParam("newStatus", "status")
            .when()
            .put(AirportProjectURIs.FLIGHTS+"/update-flight-status/1")
            .then()
            .statusCode(400);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void cancelFlight_returns_403() {
        given()
            .queryParam("newStatus", "status")
            .when()
            .put(AirportProjectURIs.FLIGHTS+"/update-flight-status/1")
            .then()
            .statusCode(403);
    }

    @Test
    @TestTransaction
    void cancelFlight_returns_401() {
        given()
            .queryParam("newStatus", "status")
            .when()
            .put(AirportProjectURIs.FLIGHTS+"/update-flight-status/1")
            .then()
            .statusCode(401);
    }

}
