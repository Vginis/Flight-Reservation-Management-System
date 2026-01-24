package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.persistence.JPATest;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineUpdateRepresentation;
import org.acme.util.AirlineUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
class AirlineResourceTest extends JPATest {

    AirlineCreateRepresentation airlineCreateRepresentation;
    AirlineUpdateRepresentation airlineUpdateRepresentation;
    @BeforeEach
    void setup() {
        airlineCreateRepresentation = AirlineUtil.createAirlineCreateRepresentation();
        airlineCreateRepresentation.setU2digitCode("AB");

        airlineUpdateRepresentation = AirlineUtil.createAirlineUpdateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_searchAirlines_success() {

        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .when()
            .get(AirportProjectURIs.AIRLINES)
            .then()
            .statusCode(200)
            .body("results.size()", greaterThanOrEqualTo(2))
            .body("results.u2digitCode", hasItems("RR", "AA"));
    }

    @Test
    void test_searchAirlines_returns_401() {
        given()
                .queryParam("size", 10)
                .queryParam("index", 0)
                .when()
                .get(AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_getAirlineLogos_success() {

        given()
            .contentType(ContentType.JSON)
            .body(List.of("RR"))
            .when()
            .post(AirportProjectURIs.AIRLINES+"/logos")
            .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void test_getAirlineLogos_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(List.of("RR"))
            .when()
            .post(AirportProjectURIs.AIRLINES+"/logos")
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void createAirline() {
        given()
            .contentType(ContentType.JSON)
            .body(airlineCreateRepresentation)
            .when()
            .post(AirportProjectURIs.AIRLINES)
            .then()
            .statusCode(201);
    }

    @Test
    @TestTransaction
    void createAirline_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(airlineCreateRepresentation)
            .when()
            .post(AirportProjectURIs.AIRLINES)
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void createAirline_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(airlineCreateRepresentation)
                .when()
                .post(AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_updateAirline() {
        given()
            .contentType(ContentType.JSON)
            .body(airlineUpdateRepresentation)
            .when()
            .put(AirportProjectURIs.AIRLINES)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.AIRLINE_UPDATE_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_updateAirline_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(airlineUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_updateAirline_returns_403() {
        given()
                .contentType(ContentType.JSON)
                .body(airlineUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(403);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_deleteAirline() {
        given()
            .when()
            .delete(AirportProjectURIs.AIRLINES+"/2")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.AIRLINE_DELETION_SUCCESS));
    }

    @Test
    @TestTransaction
    void test_deleteAirline_returns_401() {
        given()
                .when()
                .delete(AirportProjectURIs.AIRLINES+"/2")
                .then()
                .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_deleteAirline_returns_403() {
        given()
                .when()
                .delete(AirportProjectURIs.AIRLINES+"/2")
                .then()
                .statusCode(403);
    }
}
