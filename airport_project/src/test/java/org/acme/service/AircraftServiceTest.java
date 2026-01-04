package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.aircraft.AircraftCreateUpdateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.AircraftUtil;
import org.acme.util.AirlineUtil;
import org.acme.util.UserContext;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
    @Mock
    UserContext userContext;
    @Mock
    AirlineAdministratorRepository airlineAdministratorRepository;

    Aircraft aircraft;
    AircraftRepresentation aircraftRepresentation;
    AircraftCreateUpdateRepresentation aircraftCreateUpdateRepresentation;
    Airline airline;
    AirlineAdministrator airlineAdministrator;
    @BeforeEach
    void setup(){
       aircraft = AircraftUtil.createAircraft();
       aircraftRepresentation = AircraftUtil.createAircraftRepresentation();
       aircraftCreateUpdateRepresentation = AircraftUtil.createAircraftCreateRepresentation();
       airline = AirlineUtil.createAirline();
       airlineAdministrator = UserUtil.createAirlineAdministrator(airline);
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
    void testSmartSearchAircraft() {
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(airlineAdministratorRepository.findByUsername("user-1"))
            .thenReturn(Optional.ofNullable(airlineAdministrator));
        Mockito.when(aircraftRepository.smartSearchAircraft("aircraft-1", airline))
            .thenReturn(List.of(aircraft));
        Mockito.when(aircraftMapper.map(Mockito.anyList())).thenReturn(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> aircraftService.smartSearchAircraft("aircraft-1"));
    }

    @Test
    void testSmartSearchAircraftThrowsNotFoundException() {
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(airlineAdministratorRepository.findByUsername("user-1"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> aircraftService.smartSearchAircraft("aircraft-1"));
    }

    @Test
    void testCreateAircraft(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(airlineAdministratorRepository.findByUsername("user-1"))
                .thenReturn(Optional.of(airlineAdministrator));
        Mockito.doNothing().when(aircraftRepository).persist(Mockito.any(Aircraft.class));
        Assertions.assertDoesNotThrow(() -> aircraftService.createAircraft(aircraftCreateUpdateRepresentation));
    }

    @Test
    void testCreateAircraftThrowsNotFoundException(){
        Mockito.when(userContext.extractUsername()).thenReturn("user-1");
        Mockito.when(airlineAdministratorRepository.findByUsername("user-1"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                aircraftService.createAircraft(aircraftCreateUpdateRepresentation));
    }

    @Test
    void testUpdateAircraft(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(aircraft));
        Mockito.when(aircraftRepository.getEntityManager())
                .thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(Aircraft.class)))
                .thenReturn(aircraft);
        Assertions.assertDoesNotThrow(() -> aircraftService.updateAircraft(aircraftCreateUpdateRepresentation,1));
    }

    @Test
    void testUpdateAircraftThrowsNotFoundException(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                aircraftService.updateAircraft(aircraftCreateUpdateRepresentation, 1));
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
