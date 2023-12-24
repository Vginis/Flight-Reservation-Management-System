package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.acme.persistence.JPATest;
import org.acme.representation.FlightRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
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

}
