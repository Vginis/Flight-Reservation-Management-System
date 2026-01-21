package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Airline;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class AirlineJPATest extends JPATest {

    @Inject
    AirlineRepository airlineRepository;

    @Test
    void listAirlines() {
        List<?> result = em.createQuery("select c from Airline c").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void test_findAirlineBy2DCode() {
        Optional<Airline> airline = airlineRepository.findOptionalAirlineByU2DigitCode("RR");
        Assertions.assertTrue(airline.isPresent());
    }

    @Test
    void testFindAirlineByName() {
        Optional<Airline> airline = airlineRepository.findOptionalAirlineByName("Aegean Airlines");
        Assertions.assertTrue(airline.isPresent());
    }

    @Test
    void testSearchAirportsByParams() {
        PageQuery<AirlineSortAndFilterBy> query = new PageQuery<>(
                AirlineSortAndFilterBy.AIRLINE_NAME, "Air", 5, 0,
                AirlineSortAndFilterBy.AIRLINE_NAME, SortDirection.ASCENDING
        );
        PageResult<Airline> airlinePageResult = airlineRepository.searchAirlinesByParams(query);
        Assertions.assertEquals(2, airlinePageResult.getResults().size());
        Assertions.assertEquals("AA", airlinePageResult.getResults().getFirst().getU2digitCode());
    }
}
