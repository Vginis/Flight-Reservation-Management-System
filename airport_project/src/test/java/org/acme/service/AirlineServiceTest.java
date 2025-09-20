package org.acme.service;

import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Airline;
import org.acme.mapper.AirlineMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.airline.AirlineRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.AirlineUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AirlineServiceTest {
    @InjectMocks
    AirlineService airlineService;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    AirlineMapper airlineMapper;

    Airline airline;
    AirlineRepresentation airlineRepresentation;

    @BeforeEach
    void setup(){
        airline = AirlineUtil.createAirline();
        airlineRepresentation = AirlineUtil.createAirlineRepresentation();
    }

    @Test
    void testSearchByParams(){
        PageQuery<AirlineSortAndFilterBy> query = new PageQuery<>(
                AirlineSortAndFilterBy.AIRLINE_NAME, "airline", 5, 0,
                AirlineSortAndFilterBy.AIRLINE_NAME, SortDirection.ASCENDING
        );
        PageResult<Airline> airlinePageResult = new PageResult<>(1, List.of(airline));

        Mockito.when(airlineRepository.searchAirlinesByParams(query))
                .thenReturn(airlinePageResult);
        Mockito.when(airlineMapper.map(airlinePageResult)).thenReturn(
                new PageResult<>(1, List.of(airlineRepresentation)));
        PageResult<AirlineRepresentation> pageResult = airlineService.searchAirlinesByParams(query);
        Assertions.assertEquals(1,  pageResult.getTotal());
    }
}
