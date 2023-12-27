package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.acme.persistence.JPATest;
import io.quarkus.test.TestTransaction;
import org.acme.representation.FlightRepresentation;
import org.acme.util.Fixture;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class FlightResourceTest extends JPATest {

    @Test
    public void findAllFlights() {
        List<FlightRepresentation> flights = when().get(Fixture.API_ROOT+AirportProjectURIs.FLIGHTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(4, flights.size());
    }

    @Test
    public void findFlightByAirlineId() throws JsonMappingException, JsonProcessingException {
        List<FlightRepresentation> flights = given().queryParam("airlineId", 4).when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {}) ;
        assertEquals(2, flights.size());
    }

    @Test
    public void findFlightByDepartureAirport(){
        List<FlightRepresentation> flights = when().get(Fixture.API_ROOT+AirportProjectURIs.FLIGHTS + "/departure/Eleftherios Venizelos")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1, flights.size());
        assertEquals("Eleftherios Venizelos",flights.get(0).departureAirport);
        assertEquals("Fiumicino",flights.get(0).arrivalAirport);
    }

    @Test
    public void findFlightByArrivalAirport(){
        List<FlightRepresentation> flights = when().get(Fixture.API_ROOT+AirportProjectURIs.FLIGHTS + "/arrival/Fiumicino")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1, flights.size());
        assertEquals("Fiumicino",flights.get(0).arrivalAirport);
        assertEquals("Eleftherios Venizelos",flights.get(0).departureAirport);
    }

    @Test
    public void findFlightByArrivalTime(){
        List<FlightRepresentation> flights = when().get(Fixture.API_ROOT+AirportProjectURIs.FLIGHTS + "/arrivals" + "/" + "2023-07-19T21:00:00")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(2, flights.size());
        assertEquals(LocalDateTime.parse("2023-07-19T21:00:00"),flights.get(0).arrivalTime);
    }

    @Test
    public void findFlightByDepartureTime(){
        List<FlightRepresentation> flights = when().get(Fixture.API_ROOT+AirportProjectURIs.FLIGHTS + "/departures" + "/" + "2023-07-18T21:00:00")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1, flights.size());
        assertEquals(LocalDateTime.parse("2023-07-18T21:00:00"),flights.get(0).departureTime);
    }

    @Test
    public void findExistingFlight() {
        FlightRepresentation f = when().get(Fixture.API_ROOT+ AirportProjectURIs.FLIGHTS +"/"+ 7)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        assertEquals("FR8438", f.flightNo);
    }

    @Test
    public void findNoExistingFlight() {
        when().get(Fixture.API_ROOT+ AirportProjectURIs.FLIGHTS +"/"+ 1000)
                .then()
                .statusCode(404);
    }

    @Test
    public void createFlight() {
        FlightRepresentation flightRepresentation = Fixture.getFlightRepresentation();
        FlightRepresentation savedFlight = given()
                .contentType(ContentType.JSON)
                .body(flightRepresentation)
                .when()
                .post(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS)
                .then().statusCode(201)
                .extract().as(FlightRepresentation.class);

        assertEquals("A3651", savedFlight.flightNo);
        assertEquals("Aegean Airlines",savedFlight.airlineName);
        assertEquals("Fiumicino", savedFlight.departureAirport);
    }

    @Test
    public void updateFlight() {
        FlightRepresentation flight = when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        flight.flightNo = "A3651";
        flight.airlineName = "Aegean Airlines";
        flight.departureAirport = "Fiumicino";
        flight.departureTime = LocalDateTime.parse("2024-02-12T10:12:12");
        flight.arrivalAirport = "Eleftherios Venizelos";
        flight.arrivalTime = LocalDateTime.parse("2024-02-12T18:24:36");
        flight.aircraftCapacity = 120;
        flight.aircraftType = "BSA-4545";
        flight.ticketPrice = (long) 80;
        flight.availableSeats = 24;
        flight.ticketList = new ArrayList<>();

        given().contentType(ContentType.JSON).body(flight)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then().statusCode(204);

        FlightRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        assertEquals("A3651", updated.flightNo);
    }

    @Test
    public void updateFlightWithNotTheSameId() {
        FlightRepresentation flight = when().get(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 7)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);
        flight.id = 15;

        given().contentType(ContentType.JSON).body(flight)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 7)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    public void removeExistingFlight(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
