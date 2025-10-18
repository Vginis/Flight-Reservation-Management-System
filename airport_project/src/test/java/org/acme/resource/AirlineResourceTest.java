package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.airline.AirlineRepresentation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class AirlineResourceTest extends JPATest {

    public static final String API_ROOT  = "http://localhost:8081";

    @Test
    void findAllAirlines() {
        List<AirlineRepresentation> airlines = when().get(API_ROOT + AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirlineRepresentation>>() {});

        assertEquals(4, airlines.size());
    }

    @Test
    void findAirlineByName() {
        List<AirlineRepresentation> airlines = given().queryParam("name", "Aegean Airlines").when().get(API_ROOT + AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirlineRepresentation>>() {});

        assertEquals(1, airlines.size());
    }

    @Test
    void findExistingAirline() {
        AirlineRepresentation a1 = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);

        assertEquals("FR", a1.getU2digitCode());
    }

    @Test
    void findNoExistingAirline() {
        when().get(API_ROOT+ AirportProjectURIs.AIRLINES + "/" + 10)
                .then()
                .statusCode(404);
    }

    @Test
    void verifyMostPopularAirportByAirline() {
        String popularAirport = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/MostPopularAirport/4")
                .then().statusCode(200).extract().asString();
        assertNotNull(popularAirport);
        assertEquals("Fiumicino", popularAirport);
    }

    @Test
    public void verifyAirlineCompleteness() {
        String airlineCompleteness = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/Completeness/4")
                .then().statusCode(200).extract().asString();
        assertEquals("33.61423220973782", airlineCompleteness);
    }

    @Test
    public void createAirline() {
        AirlineRepresentation airlineRepresentation = new AirlineRepresentation();
        AirlineRepresentation savedAirline = given().contentType(ContentType.JSON).body(airlineRepresentation).when()
                                            .post(API_ROOT + AirportProjectURIs.AIRLINES).then().statusCode(201).extract().as(AirlineRepresentation.class);

        assertNotNull(savedAirline);
        assertEquals("British Airways", savedAirline.getAirlineName());
    }

    @Test
    void updateAirline() {
        AirlineRepresentation airline = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        airline.setAirlineName("Pao Airlines");
        airline.setU2digitCode("TT");

        given()
                .contentType(ContentType.JSON)
                .body(airline)
                .when().put(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(204);

        AirlineRepresentation updated = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);

        assertEquals("Pao Airlines", updated.getAirlineName());
    }

    @Test
    void updateAirlineWithNotTheSameId() {
        AirlineRepresentation airline = when().get(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        airline.setId(10);
        given().contentType(ContentType.JSON).body(airline)
                .when().put(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    void removeExistingAirline() {
        when()
                .delete(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 13)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    void removeNoExistingAirline() {
        when()
                .delete(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 3)
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    void makeNewFlight() {
        when().post(API_ROOT + AirportProjectURIs.AIRLINES + "/4/makeFlight/A36543/ATH/SPA/ShitPlane/20/100")
                .then().statusCode(201)
                .header("Location", Matchers.matchesPattern(".*/Flights/[0-9]+"));
    }

    @Test
    void denyAddingWrongAirlineFlight() {
        when()
                .post(API_ROOT + "/5/makeFlight/A36543/ATH/SPA/ShitPlane/20/100")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void denyAddingExistingFlight() {
        when().post(API_ROOT + AirportProjectURIs.AIRLINES + "/4/makeFlight/A36543/ATH/SPA/ShitPlane/20/100")
                .then().statusCode(201)
                .header("Location", Matchers.matchesPattern(".*/Flights/[0-9]+"));
        when()
                .post(API_ROOT + "/4/makeFlight/A36543/ATH/SPA/ShitPlane/20/100")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @TestTransaction
    void deleteExistingFlight() {
        when()
                .delete(API_ROOT + AirportProjectURIs.AIRLINES + "/4/deleteFlight/A3653/FCO/SPA")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    void deleteNoExistingFlight() {
        when()
                .delete(API_ROOT + AirportProjectURIs.AIRLINES + "/4/deleteFlight/A3650/FCO/SPA")
                .then()
                .statusCode(404);
    }

}
