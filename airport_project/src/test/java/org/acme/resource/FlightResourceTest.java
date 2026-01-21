package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.persistence.JPATest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;

@QuarkusTest
class FlightResourceTest extends JPATest {

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_searchFlights() {

        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
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
                .queryParam("size", 10)
                .queryParam("index", 0)
                .when()
                .get(AirportProjectURIs.FLIGHTS+"/flight-seat-layout/123e4567-e89b-12d3-a456-426614174000")
                .then()
                .statusCode(200);
    }

    @Test
    void test_getFlightLayoutByUUID_returns_401() {
        given()
                .queryParam("size", 10)
                .queryParam("index", 0)
                .when()
                .get(AirportProjectURIs.FLIGHTS+"/flight-seat-layout/123e4567-e89b-12d3-a456-426614174000")
                .then()
                .statusCode(401);
    }
}
