package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Airline;
import org.acme.mapper.AirlineMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.airline.AirlineRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
@QuarkusTest
public class AirlineMapperTest {

    @Inject
    AirlineMapper airlineMapper;
    @Inject
    AirlineRepository airlineRepository;

//    private Flight findFlight(List<Flight> flights,String flightNo){
//        return flights.stream().filter(a -> a.getFlightNo().contains(flightNo))
//                .findFirst().orElse(null);
//    }

    @Transactional
    @Test
    void testToModel(){

        AirlineRepresentation airlineRepresentation = Fixture.getAirlineRepresentation();
        Airline entity = airlineMapper.toModel(airlineRepresentation);

        assertEquals(entity.getAirlineName(), airlineRepresentation.getAirlineName());
        assertEquals(entity.getU2digitCode(), airlineRepresentation.getU2digitCode());
        assertEquals(entity.getFlights().size(), airlineRepresentation.getFlights().size());
    }

//    @Transactional
//    @Test
//    void testToRepresentation(){
//        Airline airline = airlineRepository.findById(5);
//
//        AirlineRepresentation airlineRepresentation = airlineMapper.toRepresentation(airline);
//        assertEquals(airline.getAirlineName(),airlineRepresentation.getAirlineName());
//        assertEquals(airline.getFlights().get(0).getFlightNo(),airlineRepresentation.getFlights().get(0).flightNo);
//        assertEquals(airline.getU2digitCode(),airlineRepresentation.getU2digitCode());
//
//        List<Flight> flightList = airline.getFlights();
//        assertEquals(1,airlineRepresentation.getFlights().size());
//        for (FlightRepresentation r : airlineRepresentation.getFlights()){
//            Flight f = findFlight(flightList, r.flightNo);
//            assertEquals(f.getFlightNo(), r.flightNo);
//            assertEquals(f.getId(), r.id);
//            assertEquals(f.getAircraftCapacity(), r.aircraftCapacity);
//            assertEquals(f.getAirline().getAirlineName(), r.airlineName);
//            assertEquals(f.getTicketPrice(),r.ticketPrice);
//            assertEquals(f.getAvailableSeats(), r.availableSeats);
//            assertEquals(f.getAircraftCapacity(),r.aircraftCapacity);
//            assertEquals(f.getArrivalAirport().getAirportName(),r.arrivalAirport);
//            assertEquals(f.getDepartureAirport().getAirportName(),r.departureAirport);
//            assertEquals(f.getAircraftType(),r.aircraftType);
//            assertEquals(f.getTicketList().get(0).getTicketId(),r.ticketList.get(0));
//            assertEquals(f.getDepartureTime(),r.departureTime);
//            assertEquals(f.getArrivalTime(),r.arrivalTime);
//
//        }
//
//    }
}
