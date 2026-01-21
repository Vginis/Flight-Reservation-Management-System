package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Airport;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class AirportJPATest extends JPATest {

    @Inject
    AirportRepository airportRepository;

    @Test
    void listAirports() {
        List<?> result = em.createQuery("select a from Airport a").getResultList();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void testSearchAirportsByParams() {
        PageQuery<AirportSortAndFilterBy> query = new PageQuery<>(
                AirportSortAndFilterBy.AIRPORT_NAME, "Airport", 2, 0,
                AirportSortAndFilterBy.AIRPORT_NAME, SortDirection.ASCENDING
        );

        PageResult<Airport> airportPageResult = airportRepository.searchAirportsByParams(query);
        Assertions.assertEquals(3, airportPageResult.getTotal());
        Assertions.assertEquals(2, airportPageResult.getResults().size());
        Assertions.assertEquals("ATH", airportPageResult.getResults().getFirst().getU3digitCode());
    }

    @Test
    void testSmartSearchAirports() {
        List<Airport> airports = airportRepository.smartSearchAirports("ATH");
        Assertions.assertEquals(1, airports.size());
        Assertions.assertEquals("ATH", airports.getFirst().getU3digitCode());
    }

    @Test
    void testSmartSearchAirportsNullInput() {
        List<Airport> airports = airportRepository.smartSearchAirports(null);
        Assertions.assertEquals(3, airports.size());
    }

    @Test
    void testFindAirportBy3DCode() {
        Optional<Airport> airport = airportRepository.findAirportBy3DCode("ATH");
        Assertions.assertTrue(airport.isPresent());
    }

    @Test
    void testFindAirportByName() {
        Optional<Airport> airport = airportRepository.findAirportByName("Fiumicino Airport");
        Assertions.assertTrue(airport.isPresent());
    }
}
