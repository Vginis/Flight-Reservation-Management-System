package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;
import org.acme.mapper.AirportMapper;
import org.acme.persistence.AirportRepository;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AirportMapperTest {

    @Inject
    AirportMapper airportMapper;
    @Inject
    AirportRepository airportRepository;

    @Transactional
    @Test
    void testToModel(){
        AirportRepresentation airportRepresentation = Fixture.getAirportRepresentation();
        Airport entity = airportMapper.toModel(airportRepresentation);

        assertEquals(entity.getAirportId(), airportRepresentation.airportId);
        assertEquals(entity.getAirportName(), airportRepresentation.airportName);
        assertEquals(entity.getCity(), airportRepresentation.city);
        assertEquals(entity.getCountry(), airportRepresentation.country);
        assertEquals(entity.getU3digitCode(), airportRepresentation.u3digitCode);
        assertEquals(entity.getDepFlights().size(), airportRepresentation.depFlights.size());
        assertEquals(entity.getArrFlights().size(), airportRepresentation.arrFlights.size());
    }

    @Test
    void testToRepresentation(){
        Airport airport = airportRepository.findById(2);
        AirportRepresentation airportRepresentation = airportMapper.toRepresentation(airport);

        assertEquals(airport.getAirportId(), airportRepresentation.airportId);
        assertEquals(airport.getAirportName(), airportRepresentation.airportName);
        assertEquals(airport.getCity(), airportRepresentation.city);
        assertEquals(airport.getCountry(), airportRepresentation.country);
        assertEquals(airport.getU3digitCode(), airportRepresentation.u3digitCode);
        assertEquals(airport.getDepFlights().size(), airportRepresentation.depFlights.size());
        assertEquals(airport.getArrFlights().size(), airportRepresentation.arrFlights.size());
    }
}
