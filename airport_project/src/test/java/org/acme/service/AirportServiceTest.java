package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Airport;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirportMapper;
import org.acme.persistence.AirportRepository;
import org.acme.representation.airport.AirportCreateRepresentation;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.representation.airport.AirportUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.AirportUtil;
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
class AirportServiceTest {
    @InjectMocks
    AirportService airportService;
    @Mock
    AirportRepository airportRepository;
    @Mock
    AirportMapper airportMapper;
    @Mock
    EntityManager entityManager;

    Airport airport;
    AirportRepresentation airportRepresentation;
    AirportCreateRepresentation airportCreateRepresentation;
    AirportUpdateRepresentation airportUpdateRepresentation;
    @BeforeEach
    void setup(){
        airport = AirportUtil.createAirport();
        airportRepresentation = AirportUtil.createAirportRepresentation();
        airportCreateRepresentation = AirportUtil.createAirportCreateRepresentation();
        airportUpdateRepresentation = AirportUtil.createAirportUpdateRepresentation();
    }

    @Test
    void testSearchByParams(){
        PageQuery<AirportSortAndFilterBy> query = new PageQuery<>(
                AirportSortAndFilterBy.AIRPORT_NAME, "airport", 5, 0,
                AirportSortAndFilterBy.AIRPORT_NAME, SortDirection.ASCENDING
        );
        PageResult<Airport> airportPageResult = new PageResult<>(1, List.of(airport));

        Mockito.when(airportRepository.searchAirportsByParams(query))
                .thenReturn(airportPageResult);
        Mockito.when(airportMapper.map(airportPageResult)).thenReturn(
                new PageResult<>(1, List.of(airportRepresentation)));
        PageResult<AirportRepresentation> pageResult = airportService.searchAirportsByParams(query);
        Assertions.assertEquals(1,  pageResult.getTotal());
    }

    @Test
    void testCreateAirport(){
        Mockito.when(airportRepository.findAirportByName("airport 1"))
                .thenReturn(Optional.empty());
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.empty());
        Mockito.doNothing().when(airportRepository).persist(Mockito.any(Airport.class));
        Assertions.assertDoesNotThrow(() ->
                airportService.createAirport(airportCreateRepresentation));
    }

    @Test
    void testCreateAirportThrowsInvalidRequestException(){
        Mockito.when(airportRepository.findAirportByName("airport 1"))
                .thenReturn(Optional.empty());
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(airport));
        Assertions.assertThrows(InvalidRequestException.class,
                () -> airportService.createAirport(airportCreateRepresentation));
    }

    @Test
    void testUpdateAirportDetails(){
        Mockito.when(airportRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airport));
        Mockito.when(airportRepository.findAirportByName("airport 2"))
                .thenReturn(Optional.empty());
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.empty());
        Mockito.when(airportRepository.getEntityManager())
                .thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(Airport.class)))
                .thenReturn(airport);
        Assertions.assertDoesNotThrow(() ->
                airportService.updateAirport(airportUpdateRepresentation));
    }

    @Test
    void testUpdateAirportDetailsThrowsResourceNotFoundException(){
        Mockito.when(airportRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,() ->
                airportService.updateAirport(airportUpdateRepresentation));
    }

    @Test
    void testUpdateAirportDetailsThrowsInvalidRequestException(){
        Airport otherAirport = new Airport("airport 2", "Greece","Athens","BTH");
        Mockito.when(airportRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airport));
        Mockito.when(airportRepository.findAirportByName("airport 2"))
                .thenReturn(Optional.of(otherAirport));
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidRequestException.class,() ->
                airportService.updateAirport(airportUpdateRepresentation));
    }

    @Test
    void testDeleteAirport(){
        Mockito.when(airportRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airport));
        Mockito.doNothing().when(airportRepository).delete(airport);
        Assertions.assertDoesNotThrow(() -> airportService.deleteAirport(1));
    }

    @Test
    void testDeleteAirportDetailsThrowsResourceNotFoundException(){
        Mockito.when(airportRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,() ->
                airportService.deleteAirport(1));
    }
}
