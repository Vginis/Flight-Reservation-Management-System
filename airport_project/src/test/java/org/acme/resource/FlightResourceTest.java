package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.acme.persistence.JPATest;
import org.acme.representation.FlightRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class FlightResourceTest extends JPATest {

    @Test
    public void findFlight() throws JsonMappingException, JsonProcessingException {
        List<FlightRepresentation> flights = given().queryParam("airlineId", 4).when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHT)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {}) ;
        assertEquals(1, flights.size());

    }

    //TODO (Kkostakis work in progress)
    @Test
    public void createFlight() {

        FlightRepresentation flightRepresentation = Fixture.getFlightRepresentation();
        FlightRepresentation createdFlight = given().contentType(ContentType.JSON).body(flightRepresentation).when()
                .put(Fixture.API_ROOT + AirportProjectURIs.FLIGHT).then().statusCode(201).header("Location", Fixture.API_ROOT + AirportProjectURIs.FLIGHT + "/9")
                .extract().as(FlightRepresentation.class);

        assertEquals(9, createdFlight.id);
    }

    //TODO (Kkostakis work in progress)
    @Test
    public void updateFlight() {
        FlightRepresentation flight = when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHT + "/" + 4)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        flight.flightNo = "A4651";

        given()
                .contentType(ContentType.JSON)
                .body(flight)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.FLIGHT + "/" + 4)
                .then().statusCode(204);


        FlightRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHT + "/" + 4)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        assertEquals("A4651", updated.flightNo);
    }

}
