package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class FlightMapperTest {

    @Inject
    FlightMapper flightMapper;

    @Inject
    FlightRepository flightRepository;

    @Test
    @Transactional
    public void testToRepresentation() {
        Flight flight = flightRepository.findById(7);
        FlightRepresentation flightRepresentation = flightMapper.toRepresentation(flight);

        assertEquals(flight.getId(), flightRepresentation.id);
        assertEquals(flight.getFlightNo(), flightRepresentation.flightNo);
        assertEquals(flight.getAirline().getAirlineName(), flightRepresentation.airlineName);
        assertEquals(flight.getDepartureAirport().getAirportName(), flightRepresentation.departureAirport);
        assertEquals(flight.getDepartureTime(), flightRepresentation.departureTime);
        assertEquals(flight.getArrivalAirport().getAirportName(), flightRepresentation.arrivalAirport);
        assertEquals(flight.getArrivalTime(), flightRepresentation.arrivalTime);
        assertEquals(flight.getAircraftCapacity(), flightRepresentation.aircraftCapacity);
        assertEquals(flight.getAircraftType(), flightRepresentation.aircraftType);
        assertEquals(flight.getTicketPrice(), flightRepresentation.ticketPrice);
        assertEquals(flight.getAvailableSeats(), flightRepresentation.availableSeats);
        for (Integer r : flightRepresentation.ticketList) {
            Integer b = Objects.requireNonNull(flight.getTicketList().stream().filter(a -> a.getTicketId().equals(r)).findFirst().orElse(null)).getTicketId();
            assert b != null;
            assertEquals(b, r);
        }
    }

    @Test
    @Transactional
    public void testToModel() {
        FlightRepresentation flightRepresentation = Fixture.getFlightRepresentation();
        Flight entity = flightMapper.toModel(flightRepresentation);

        assertEquals(entity.getId(), flightRepresentation.id);
        assertEquals(entity.getFlightNo(), flightRepresentation.flightNo);
        assertEquals(entity.getAirline().getAirlineName(), flightRepresentation.airlineName);
        assertEquals(entity.getDepartureAirport().getAirportName(), flightRepresentation.departureAirport);
        assertEquals(entity.getDepartureTime(), flightRepresentation.departureTime);
        assertEquals(entity.getArrivalAirport().getAirportName(), flightRepresentation.arrivalAirport);
        assertEquals(entity.getArrivalTime(), flightRepresentation.arrivalTime);
        assertEquals(entity.getAircraftCapacity(), flightRepresentation.aircraftCapacity);
        assertEquals(entity.getAircraftType(), flightRepresentation.aircraftType);
        assertEquals(entity.getTicketPrice(), flightRepresentation.ticketPrice);
        assertEquals(entity.getAvailableSeats(), flightRepresentation.availableSeats);
        assertEquals(1, flightRepresentation.ticketList.size());
    }

}
