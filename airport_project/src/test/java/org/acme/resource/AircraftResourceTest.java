package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.representation.aircraft.AircraftCreateUpdateRepresentation;
import org.acme.util.AircraftUtil;
import org.acme.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class AircraftResourceTest {

    @InjectMock
    UserContext userContext;

    AircraftCreateUpdateRepresentation aircraftCreateUpdateRepresentation;
    @BeforeEach
    void setup() {
        aircraftCreateUpdateRepresentation = AircraftUtil.createAircraftCreateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void test_searchAircraft_success() {

        given()
                .queryParam("size", 10)
                .queryParam("index", 0)
                .when()
                .get(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(200)
                .body("results.size()", greaterThanOrEqualTo(2));
    }

    @Test
    @TestSecurity(user = "user", roles = {Role.PASSENGER})
    void test_searchAircrafts_wrongRole_forbidden() {
        given()
                .when()
                .get(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(403);
    }

    @Test
    void test_searchAircrafts_unauthenticated_unauthorized() {
        given()
                .when()
                .get(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void smartSearchAircrafts() {
        Mockito.when(userContext.extractUsername()).thenReturn("admin1");
        given()
                .queryParam("aircraftName", "Boeing")
                .when()
                .get(AirportProjectURIs.AIRCRAFTS+"/smart-search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void test_createAircraft() {
        Mockito.when(userContext.extractUsername()).thenReturn("admin1");
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(200);
    }

    @Test
    @TestTransaction
    void test_createAircraft_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_createAircraft_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRCRAFTS)
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void test_updateAircraft() {
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRCRAFTS+"/1")
                .then()
                .statusCode(200)
                .body("key", equalTo(SuccessMessages.AIRCRAFT_UPDATE_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_updateAircraft_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRCRAFTS+"/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_updateAircraft_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(aircraftCreateUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRCRAFTS+"/1")
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.AIRLINE_ADMINISTRATOR})
    void test_deleteAircraft() {
        given()
                .when()
                .delete(AirportProjectURIs.AIRCRAFTS+"/2")
                .then()
                .statusCode(200)
                .body("key", equalTo(SuccessMessages.AIRCRAFT_DELETION_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_deleteAircraft_returns_401() {
        given()
                .when()
                .delete(AirportProjectURIs.AIRCRAFTS+"/1")
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_deleteAircraft_returns_403() {
        given()
                .when()
                .delete(AirportProjectURIs.AIRCRAFTS+"/1")
                .then()
                .statusCode(403);
    }
}