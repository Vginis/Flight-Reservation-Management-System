package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.PassengerCreateRepresentation;
import org.acme.service.PassengerService;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class PassengerResourceTest {

    @InjectMock
    PassengerService passengerService;

    PassengerCreateRepresentation passengerCreateRepresentation;
    PassengerUpdateRepresentation passengerUpdateRepresentation;
    @BeforeEach
    void setup() {
        passengerCreateRepresentation = UserUtil.createPassengerCreateRepresentation();
        passengerUpdateRepresentation = UserUtil.createPassengerUpdateRepresentation();
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void testCreatePassenger_success() {
        Mockito.doNothing().when(passengerService).createPassengerAsAdmin(passengerCreateRepresentation);
        given()
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .post(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.PASSENGER_CREATE_SUCCESS));
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void testCreatePassenger_returns_403() {
        given()
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .post(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(403);
    }

    @Test
    void testCreatePassenger_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .post(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void testCompletePassengerRegistration_success() {
        Mockito.doNothing().when(passengerService).completePassengerRegistration(passengerCreateRepresentation);
        given()
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .post(AirportProjectURIs.PASSENGERS+"/complete-registration")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.PASSENGER_CREATE_SUCCESS));
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void testCompletePassengerRegistration_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .post(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(403);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void testGetPassengerPassport_success() {
        given()
            .queryParam("username", "passenger1")
            .when()
            .get(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(200);
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void testGetPassengerPassport_returns_403() {
        given()
            .queryParam("username", "passenger1")
            .when()
            .get(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(403);
    }

    @Test
    void testGetPassengerPassport_returns_401() {
        given()
            .queryParam("username", "passenger1")
            .when()
            .get(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void testUpdatePassenger_success() {
        given()
            .queryParam("username", "passenger1")
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .put(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(200);
    }

    @Test
    @TestTransaction
    void testUpdatePassenger_returns_401() {
        given()
            .queryParam("username", "passenger1")
            .contentType(ContentType.JSON)
            .body(passengerCreateRepresentation)
            .when()
            .put(AirportProjectURIs.PASSENGERS)
            .then()
            .statusCode(401);
    }
}
