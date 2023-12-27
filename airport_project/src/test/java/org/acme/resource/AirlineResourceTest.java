package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.persistence.JPATest;
import org.acme.representation.AirlineRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AirlineResourceTest extends JPATest {

    @Test
    public void findAllAirlines() {
        List<AirlineRepresentation> airlines = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirlineRepresentation>>() {});

        assertEquals(4, airlines.size());
    }

    @Test
    public void findAirlineByName() {
        List<AirlineRepresentation> airlines = given().queryParam("name", "Aegean Airlines").when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirlineRepresentation>>() {});

        assertEquals(1, airlines.size());
    }

    @Test
    public void findExistingAirline() {
        AirlineRepresentation a1 = when().get(Fixture.API_ROOT+ AirportProjectURIs.AIRLINES+"/"+ 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);

        assertEquals("FR", a1.u2digitCode);
    }

    @Test
    public void findNoExistingAirline() {
        when().get(Fixture.API_ROOT+ AirportProjectURIs.AIRLINES+"/"+ 10)
                .then()
                .statusCode(404);
    }

    @Test
    public void verifyMostPopularAirportByAirline(){
        String popularAirport = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/MostPopularAirport/4")
                .then().statusCode(200).extract().asString();
        assertNotNull(popularAirport);
        assertEquals("Fiumicino",popularAirport);
    }

    @Test
    public void verifyAirlineCompleteness(){
        String airlineCompleteness = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/Completeness/4")
                .then().statusCode(200).extract().asString();
        assertEquals("33.61423220973782",airlineCompleteness);
    }

    @Test
    public void createAirline(){
        AirlineRepresentation airlineRepresentation = Fixture.getAirlineRepresentation();
        AirlineRepresentation savedAirline = given().contentType(ContentType.JSON).body(airlineRepresentation).when()
                                            .post(Fixture.API_ROOT + AirportProjectURIs.AIRLINES).then().statusCode(201).extract().as(AirlineRepresentation.class);

        assertNotNull(savedAirline);
        assertEquals("British Airways", savedAirline.airlineName);
    }

    @Test
    public void updateAirline() {
        AirlineRepresentation airline = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        airline.airlineName = "Pao Airlines";
        airline.flights = new ArrayList<>();
        airline.u2digitCode = "TT";

        given()
                .contentType(ContentType.JSON)
                .body(airline)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(204);

        AirlineRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);

        assertEquals("Pao Airlines", updated.airlineName);
    }

    @Test
    public void updateAirlineWithNotTheSameId() {
        AirlineRepresentation airline = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        airline.id = 10;

        given().contentType(ContentType.JSON).body(airline)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    public void removeExistingAirline(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 13)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    public void removeNoExistingAirline(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 3)
                .then()
                .statusCode(404);
    }

}
