package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.representation.airport.AirportCreateRepresentation;
import org.acme.representation.airport.AirportUpdateRepresentation;
import org.acme.util.AirportUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;


@QuarkusTest
class AirportResourceTest {

    AirportCreateRepresentation airportCreateRepresentation;
    AirportUpdateRepresentation airportUpdateRepresentation;
    @BeforeEach
    void setup() {
        airportCreateRepresentation = AirportUtil.createAirportCreateRepresentation();
        airportUpdateRepresentation = AirportUtil.createAirportUpdateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_searchAirports_asSystemAdmin_success() {

        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .when()
            .get(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(200)
            .body("results.size()", greaterThanOrEqualTo(3))
            .body("results.u3digitCode", hasItems("ATH", "FCO", "MAD"));
    }

    @Test
    @TestSecurity(user = "user", roles = {Role.PASSENGER})
    void test_searchAirports_wrongRole_forbidden() {
        given()
            .when()
            .get(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(403);
    }

    @Test
    void test_searchAirports_unauthenticated_unauthorized() {
        given()
            .when()
            .get(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(401);
    }

    @Test
    void smartSearchAirports() {
        given()
            .queryParam("value", "ATH")
            .when()
            .get(AirportProjectURIs.AIRPORTS+"/smart-search")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_createAirport() {
        airportCreateRepresentation.setU3digitCode("LHR");
        airportCreateRepresentation.setAirportName("London Heethrow Airport");
        given()
            .contentType(ContentType.JSON)
            .body(airportCreateRepresentation)
            .when()
            .post(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(201);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_createAirport_returns_400() {
        given()
            .contentType(ContentType.JSON)
            .body(airportCreateRepresentation)
            .when()
            .post(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(400);
    }

    @Test
    @TestTransaction
    void test_createAirport_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(airportCreateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_createAirport_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(airportCreateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_updateAirport() {
        given()
            .contentType(ContentType.JSON)
            .body(airportUpdateRepresentation)
            .when()
            .put(AirportProjectURIs.AIRPORTS)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.AIRPORT_UPDATE_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_updateAirport_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(airportUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_updateAirport_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(airportUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_deleteAirport() {
        given()
            .when()
            .delete(AirportProjectURIs.AIRPORTS+"/1")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.AIRPORT_DELETION_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_deleteAirport_returns_401() {
        given()
            .when()
            .delete(AirportProjectURIs.AIRPORTS+"/1")
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_deleteAirport_returns_403() {
        given()
            .when()
            .delete(AirportProjectURIs.AIRPORTS+"/1")
            .then()
            .statusCode(403);
    }
}