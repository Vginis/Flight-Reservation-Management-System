package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Airline;
import org.acme.domain.Flight;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.JPATest;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class AirlineMapperTest {

    @Inject
    AirlineMapper airlineMapper;
    @Inject
    AirlineRepository airlineRepository;

    private Flight findFlight(List<Flight> flights,String flightNo){
        return flights.stream().filter(a -> a.getFlightNo().contains(flightNo))
                .findFirst().orElse(null);
    }

    @Transactional
    @Test
    void testToModel(){

        AirlineRepresentation airlineRepresentation = Fixture.getAirlineRepresentation();
        Airline entity = airlineMapper.toModel(airlineRepresentation);

        assertEquals(entity.getAirlineName(), airlineRepresentation.airlineName);
        assertEquals(entity.getU2digitCode(), airlineRepresentation.u2digitCode);
        assertEquals(entity.getFlights(), airlineRepresentation.flights);
        assertEquals(entity.getUsername(), airlineRepresentation.username);
        assertEquals(entity.getId(),airlineRepresentation.id);
        assertEquals(entity.getPassword(),airlineRepresentation.password);

    }

    @Transactional
    @Test
    void testToRepresentation(){
        Airline airline = airlineRepository.findById(4);

        AirlineRepresentation airlineRepresentation = airlineMapper.toRepresentation(airline);
        assertEquals(airline.getId(), airlineRepresentation.id);
        assertEquals(airline.getPassword(),airlineRepresentation.password);
        assertEquals(airline.getAirlineName(),airlineRepresentation.airlineName);
        assertEquals(airline.getFlights().get(0).getFlightNo(),airlineRepresentation.flights.get(0).flightNo);
        assertEquals(airline.getUsername(),airlineRepresentation.username);
        assertEquals(airline.getU2digitCode(),airlineRepresentation.u2digitCode);

        List<Flight> flightList = airline.getFlights();
        assertEquals(1,airlineRepresentation.flights.size());
        for (FlightRepresentation r : airlineRepresentation.flights){
            Flight f = findFlight(flightList, r.flightNo);
            assertEquals(f.getFlightNo(), r.flightNo);
            assertEquals(f.getId(), r.id);
            assertEquals(f.getAircraftCapacity(), r.aircraftCapacity);
            assertEquals(f.getAirline().getAirlineName(), r.airlineName);
            assertEquals(f.getTicketPrice(),r.ticketPrice);
            assertEquals(f.getAvailableSeats(), r.availableSeats);
            assertEquals(f.getAircraftCapacity(),r.aircraftCapacity);
            assertEquals(f.getArrivalAirport().getAirportName(),r.arrivalAirport);
            assertEquals(f.getDepartureAirport().getAirportName(),r.departureAirport);
            assertEquals(f.getAircraftType(),r.aircraftType);
            assertEquals(f.getTicketList().get(0).getTicketPrice(),r.ticketList.get(0).ticketPrice);
            assertEquals(f.getDepartureTime(),r.departureTime);
            assertEquals(f.getArrivalTime(),r.arrivalTime);

        }

    }
}
