package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
class AircraftJPATest extends JPATest {

    @Inject
    AircraftRepository aircraftRepository;
    @Inject
    AirlineRepository airlineRepository;

    @Test
    void listAircrafts() {
        List<?> result = em.createQuery("select a from Aircraft a").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void test_searchAircraftByParams_byName() {
        PageQuery<AircraftSortAndFilterBy> query = new PageQuery<>(
            AircraftSortAndFilterBy.AIRCRAFT_NAME, "Airbus", 5, 0,
            AircraftSortAndFilterBy.AIRCRAFT_NAME, SortDirection.ASCENDING
        );

        PageResult<Aircraft> aircraftPageResult = aircraftRepository.searchAircraftByParams(query);
        Assertions.assertEquals(1, aircraftPageResult.getResults().size());
    }

    @Test
    void test_searchAircraftByParams_byCapacity() {
        PageQuery<AircraftSortAndFilterBy> query = new PageQuery<>(
                AircraftSortAndFilterBy.AIRCRAFT_CAPACITY, "180", 5, 0,
                AircraftSortAndFilterBy.AIRCRAFT_CAPACITY, SortDirection.ASCENDING
        );

        PageResult<Aircraft> aircraftPageResult = aircraftRepository.searchAircraftByParams(query);
        Assertions.assertEquals(1, aircraftPageResult.getResults().size());
    }

    @Test
    void test_searchAircraftByParams_byAirline() {
        PageQuery<AircraftSortAndFilterBy> query = new PageQuery<>(
                AircraftSortAndFilterBy.AIRLINE, "AA", 5, 0,
                AircraftSortAndFilterBy.AIRLINE, SortDirection.ASCENDING
        );

        PageResult<Aircraft> aircraftPageResult = aircraftRepository.searchAircraftByParams(query);
        Assertions.assertEquals(2, aircraftPageResult.getResults().size());
    }

    @Test
    void test_smartSearchAircraft() {
        Airline airline = airlineRepository.findById(1);
        List<Aircraft> aircraftList = aircraftRepository.smartSearchAircraft("Airbus", airline);
        Assertions.assertEquals(1, aircraftList.size());
    }
}
