package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.flight.FlightRepresentation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class FlightResourceTest extends JPATest {

    static final String API_ROOT  = "http://localhost:8081";

    @Test
    void findAllFlights() {
        List<FlightRepresentation> flights = when().get(API_ROOT+ AirportProjectURIs.FLIGHTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(4, flights.size());
    }

    @Test
    void findFlightByAirlineId() {
        List<FlightRepresentation> flights = given().queryParam("airlineId", 4).when().get(API_ROOT + AirportProjectURIs.FLIGHTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {}) ;
        assertEquals(2, flights.size());
    }

    @Test
    void findExistingFlight() {
        FlightRepresentation f = when().get(API_ROOT+ AirportProjectURIs.FLIGHTS +"/"+ 7)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        assertEquals("FR8438", f.getFlightNumber());
    }

    @Test
    void findNoExistingFlight() {
        when().get(API_ROOT+ AirportProjectURIs.FLIGHTS +"/"+ 20)
                .then()
                .statusCode(404);
    }

    @Test
    void verifyFlightCompleteness(){
        String flightCompleteness = when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/Completeness/10")
                .then().statusCode(200).extract().asString();
        assertEquals("66.66666666666667",flightCompleteness);
    }

    @Test
    void findExistingFlightByDepartureAirport(){
        List<FlightRepresentation> flights = when().get(API_ROOT+AirportProjectURIs.FLIGHTS + "/departure/Eleftherios Venizelos")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1, flights.size());
        assertEquals("Eleftherios Venizelos",flights.getFirst().getDepartureAirport());
        assertEquals("Fiumicino",flights.getFirst().getArrivalAirport());
    }

    @Test
    void findNoExistingFlightByDepartureAirport(){
        when().get(API_ROOT+AirportProjectURIs.FLIGHTS + "/departure/Random Airport")
                .then()
                .statusCode(404);
    }

    @Test
    void findExistingFlightByArrivalAirport(){
        List<FlightRepresentation> flights = when().get(API_ROOT+AirportProjectURIs.FLIGHTS + "/arrival/Fiumicino")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1, flights.size());
        assertEquals("Fiumicino",flights.get(0).getArrivalAirport());
        assertEquals("Eleftherios Venizelos",flights.get(0).getDepartureAirport());
    }

    @Test
    void findNoExistingFlightByArrivalAirport() {
        when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/arrival/Random Airport");
    }

    @Test
    void findExistingFlightByArrivalTime(){
        List<FlightRepresentation> flights = when().get(API_ROOT+AirportProjectURIs.FLIGHTS + "/arrivals" + "/" + "2023-07-19T21:00:00")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(2, flights.size());
        assertEquals(LocalDateTime.parse("2023-07-19T21:00:00"),flights.get(0).getArrivalTime());
    }

    @Test
    void findNoExistingFlightByArrivalTime(){
        when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/arrivals/" + "2023-07-19T22:00:00")
                .then()
                .statusCode(404);
    }

    @Test
    void findExistingFlightByDepartureTime(){
        List<FlightRepresentation> flights = when().get(API_ROOT+AirportProjectURIs.FLIGHTS + "/departures" + "/" + "2023-07-19T21:00:00")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(2, flights.size());
        assertEquals(LocalDateTime.parse("2023-07-19T21:00:00"),flights.get(0).getDepartureTime());
    }

    @Test
    void findNoExistingFlightByDepartureTime(){
        when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/departures/" + "2023-07-19T22:10:00")
                .then()
                .statusCode(404);
    }

    @Test
    void createFlight() {
        FlightRepresentation flightRepresentation = new FlightRepresentation();
        FlightRepresentation savedFlight = given()
                .contentType(ContentType.JSON)
                .body(flightRepresentation)
                .when()
                .post(API_ROOT + AirportProjectURIs.FLIGHTS)
                .then().statusCode(201)
                .extract().as(FlightRepresentation.class);

        assertEquals("A3651", savedFlight.getFlightNumber());
        assertEquals("Aegean Airlines",savedFlight.getAirlineU2DigitCode());
        assertEquals("Fiumicino", savedFlight.getDepartureAirport());

        List<FlightRepresentation> flights = when().get(API_ROOT+AirportProjectURIs.FLIGHTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(5, flights.size());
    }

    @Test
    void updateFlight() {
        FlightRepresentation flight = when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

//        flight.flightNo = "A3651";
//        flight.airlineName = "Aegean Airlines";
//        flight.departureAirport = "Fiumicino";
//        flight.departureTime = LocalDateTime.parse("2024-02-12T10:12:12");
//        flight.arrivalAirport = "Eleftherios Venizelos";
//        flight.arrivalTime = LocalDateTime.parse("2024-02-12T18:24:36");
//        flight.aircraftCapacity = 120;
//        flight.aircraftType = "BSA-4545";
//        flight.ticketPrice = (long) 80;
//        flight.availableSeats = 24;
//        flight.ticketList = new ArrayList<>();

        given().contentType(ContentType.JSON).body(flight)
                .when().put(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then().statusCode(204);

        FlightRepresentation updated = when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);

        assertEquals("A3651", updated.getFlightNumber());
    }

    @Test
    void updateFlightWithNotTheSameId() {
        FlightRepresentation flight = when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 7)
                .then()
                .statusCode(200)
                .extract().as(FlightRepresentation.class);
        flight.setId(15);
        given().contentType(ContentType.JSON).body(flight)
                .when().put(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 7)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    void removeExistingFlight(){
        when()
                .delete(API_ROOT + AirportProjectURIs.FLIGHTS + "/" + 9)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void findFlightByParams() {
        List<FlightRepresentation> flights = when().get(API_ROOT + AirportProjectURIs.FLIGHTS + "/dep/" + "Eleftherios Venizelos"
                        + "/arr/" + "Fiumicino" + "/dept/" + "2023-07-19T21:00:00" + "/arrt/" + "2023-07-19T21:00:00" + "/passc/" + 8)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {}) ;
        assertEquals(1, flights.size());
    }

}
