package org.acme.resourceTest;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.acme.domain.Airport;
import org.acme.persistence.JPATest;
import org.acme.resource.*;

import org.acme.representation.AirportRepresentation;

import org.acme.util.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import java.util.List;

@QuarkusTest
public class AirportResourceTest extends JPATest {

    public static String API_ROOT  = "http://localhost:8081";
    public static Integer AIRPORT_ID = 1;

    @Test
    public void find() {
        AirportRepresentation a1 = when().get(API_ROOT+AirportProjectURIs.AIRPORTS+"/"+ AIRPORT_ID)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        Assertions.assertEquals("ATH", a1.u3digitCode);
    }

    @Test
    public void search() throws JsonMappingException, JsonProcessingException {


        List<AirportRepresentation> airports = given().queryParam("name", "Eleftherios Venizelos").when().get(API_ROOT+AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {}) ;

        Assertions.assertEquals(1, airports.size());

    }

    @Test
    public void findNonExisting() {
        when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 4711)
                .then()
                .statusCode(404);
    }

/*
    @Test
    public void update() {
        AirportRepresentation airport = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 2)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        airport.name = "New Airport";

        given()
                .contentType(ContentType.JSON)
                .body(airport)
                .when().put(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 2)
                .then().statusCode(204);


        AirportRepresentation updated = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 2)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        Assertions.assertEquals("New Airport", updated.name);
    }

    @Test
    public void create() {
        AirportRepresentation representation = Fixture.getAirportRepresentation();


        AirportRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("http://localhost:8081/Airports")
                .then().statusCode(201).extract().as(AirportRepresentation.class);


        Assertions.assertEquals(3, created.airportId);
    }*/
}