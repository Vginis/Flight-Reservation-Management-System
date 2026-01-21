package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.FlightSearchFilterParamsDTO;
import org.acme.constant.search.FlightSearchParamsDTO;
import org.acme.domain.Flight;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.search.FlightPageQuery;
import org.acme.search.PageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class FlightJPATest extends JPATest {

    @Inject
    FlightRepository flightRepository;

    @Test
    void listFlights(){
        List<?> result = em.createQuery("select p from Flight p").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getFlightByUUID_existingFlight() {
        Optional<Flight> flight = flightRepository.getFlightByUUID("123e4567-e89b-12d3-a456-426614174000");
        Assertions.assertTrue(flight.isPresent());
        Assertions.assertEquals("A3-101", flight.get().getFlightNumber());
    }

    @Test
    void getFlightByUUID_nonExistingFlight() {
        Optional<Flight> flight = flightRepository.getFlightByUUID("00000000-0000-0000-0000-000000000000");
        Assertions.assertTrue(flight.isEmpty());
    }

    @Test
    void searchFlightsByParams_departureAirport() {
        FlightSearchFilterParamsDTO filterParams = new FlightSearchFilterParamsDTO("flightNumber",
                "A3-101", "2026-01-25T09:00:00", "2026-01-25T12:30:00", 1, 3);

        FlightSearchParamsDTO flightSearchParamsDTO = new FlightSearchParamsDTO(filterParams,
                5, 0, "flightNumber", "asc");

        FlightPageQuery query = new FlightPageQuery(
                flightSearchParamsDTO,
                "2026-01-25T09:00:00",
                "2026-01-25T12:30:00",
                1,
                3
        );
        PageResult<Flight> result = flightRepository.searchFlightsByParams(query);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1, result.getResults().getFirst().getId());
    }

    @Test
    void searchFlightsByMultipleParams_arrivalAirport() {
        FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO = new FlightMultipleParamsSearchDTO();
        flightMultipleParamsSearchDTO.setArrivalAirport("MAD");

        PageResult<Flight> result = flightRepository.searchFlightsByMultipleParams(flightMultipleParamsSearchDTO, 10, 0);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals("Madrid Barajas Airport", result.getResults().getFirst().getArrivalAirport().getAirportName());
    }

    @Test
    void searchFlightsByMultipleParams_departureAndArrivalDate() {
        FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO = new FlightMultipleParamsSearchDTO();
        flightMultipleParamsSearchDTO.setDepartureDate("2026-01-25");
        flightMultipleParamsSearchDTO.setArrivalDate("2026-01-25");

        PageResult<Flight> result = flightRepository.searchFlightsByMultipleParams(flightMultipleParamsSearchDTO, 10, 0);
        Assertions.assertEquals(2, result.getTotal());
    }
}
