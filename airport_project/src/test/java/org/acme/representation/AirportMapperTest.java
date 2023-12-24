package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;
import org.acme.persistence.AirportRepository;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AirportMapperTest {

    @Inject
    AirportMapper airportMapper;
    @Inject
    AirportRepository airportRepository;

    private Airport findAirport(List<Airport> airport, String name){
        return airport.stream().filter(a -> a.getAirportName().contains(name))
                .findFirst().orElse(null);
    }

    @Transactional
    @Test
    void testToModel(){

        AirportRepresentation airportRepresentation = Fixture.getAirportRepresentation();

        Airport entity = airportMapper.toModel(airportRepresentation);

        assertEquals(entity.getAirportName(), airportRepresentation.airportName);
        assertEquals(entity.getCity(), airportRepresentation.city);
        assertEquals(entity.getCountry(), airportRepresentation.country);
        assertEquals(entity.getU3digitCode(), airportRepresentation.u3digitCode);
    }
}
