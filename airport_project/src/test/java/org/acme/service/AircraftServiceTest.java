package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.aircraft.AircraftCreateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.representation.aircraft.AircraftUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.AircraftUtil;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {
    @InjectMocks
    AircraftService aircraftService;
    @Mock
    AircraftRepository aircraftRepository;
    @Mock
    AircraftMapper aircraftMapper;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    EntityManager entityManager;

    Aircraft aircraft;
    AircraftRepresentation aircraftRepresentation;
    AircraftCreateRepresentation aircraftCreateRepresentation;
    AircraftUpdateRepresentation aircraftUpdateRepresentation;
    Airline airline;
    @BeforeEach
    void setup(){
       aircraft = AircraftUtil.createAircraft();
       aircraftRepresentation = AircraftUtil.createAircraftRepresentation();
       aircraftCreateRepresentation = AircraftUtil.createAircraftCreateRepresentation();
       aircraftUpdateRepresentation = AircraftUtil.createAircraftUpdateRepresentation();
       airline = AirlineUtil.createAirline();
    }

    @Test
    void testSearchByParams(){
        PageQuery<AircraftSortAndFilterBy> aircraftSortAndFilterByPageQuery = new PageQuery<>(
             AircraftSortAndFilterBy.AIRCRAFT_CAPACITY, "100", 5,0,
             AircraftSortAndFilterBy.AIRCRAFT_CAPACITY, SortDirection.ASCENDING
        );
        PageResult<Aircraft> pageResult = new PageResult<Aircraft>(1, List.of(aircraft));
        Mockito.when(aircraftRepository.searchAircraftByParams(aircraftSortAndFilterByPageQuery))
                .thenReturn(pageResult);
        Mockito.when(aircraftMapper.map(pageResult)).thenReturn(new PageResult<>(1, List.of(aircraftRepresentation)));

        PageResult<AircraftRepresentation> result = aircraftService.searchAircraftsByParams(aircraftSortAndFilterByPageQuery);
        Assertions.assertEquals(1, result.getTotal());
    }

    @Test
    void testCreateAircraft(){
        Mockito.when(airlineRepository.findByIdOptional(Mockito.any()))
                .thenReturn(Optional.of(airline));
        Mockito.doNothing().when(aircraftRepository).persist(Mockito.any(Aircraft.class));
        Assertions.assertDoesNotThrow(() -> aircraftService.createAircraft(aircraftCreateRepresentation));
    }

    @Test
    void testCreateAircraftThrowsNotFoundException(){
        Mockito.when(airlineRepository.findByIdOptional(Mockito.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                aircraftService.createAircraft(aircraftCreateRepresentation));
    }

    @Test
    void testUpdateAircraft(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftRepository.getEntityManager())
                .thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(Aircraft.class)))
                .thenReturn(aircraft);
        Assertions.assertDoesNotThrow(() -> aircraftService.updateAircraft(aircraftUpdateRepresentation,1));
    }

    @Test
    void testUpdateAircraftThrowsNotFoundException(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                aircraftService.updateAircraft(aircraftUpdateRepresentation, 1));
    }

    @Test
    void testDeleteAircraft(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(aircraft));
        Assertions.assertDoesNotThrow(() -> aircraftService.deleteAircraft(1));
    }

    @Test
    void testDeleteAircraftThrowsNotFoundException(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                aircraftService.deleteAircraft(1));
    }
}
