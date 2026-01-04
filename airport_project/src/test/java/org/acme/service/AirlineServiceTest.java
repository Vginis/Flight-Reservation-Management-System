package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Airline;
import org.acme.domain.AirlineLogo;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirlineMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineRepresentation;
import org.acme.representation.airline.AirlineUpdateRepresentation;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {
    @InjectMocks
    AirlineService airlineService;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    AirlineMapper airlineMapper;
    @Mock
    EntityManager entityManager;

    Airline airline;
    AirlineRepresentation airlineRepresentation;
    AirlineCreateRepresentation airlineCreateRepresentation;
    AirlineUpdateRepresentation airlineUpdateRepresentation;
    @BeforeEach
    void setup(){
        airline = AirlineUtil.createAirline();
        byte[] content = {11,12};
        airline.setLogo(new AirlineLogo("fileName", "filePath", "pdf",
                content, airline));
        airlineRepresentation = AirlineUtil.createAirlineRepresentation();
        airlineCreateRepresentation = AirlineUtil.createAirlineCreateRepresentation();
        airlineUpdateRepresentation = AirlineUtil.createAirlineUpdateRepresentation();
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
        Mockito.when(airlineMapper.map(airline)).thenReturn(
                airlineRepresentation);
        PageResult<AirlineRepresentation> pageResult = airlineService.searchAirlinesByParams(query);
        Assertions.assertEquals(1,  pageResult.getTotal());
    }

    @Test
    void testGetAirlineLogos(){
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.of(airline));
        Assertions.assertDoesNotThrow(() -> airlineService.getAirlineLogos(List.of("AA")));
    }

    @Test
    void testGetAirlineLogosThrowsNotFoundException(){
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,()
                -> airlineService.getAirlineLogos(List.of("AA")));
    }

    @Test
    void testCreateAirline(){
        Mockito.when(airlineRepository.findOptionalAirlineByName("airline 1"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.empty());
        Mockito.doNothing().when(airlineRepository).persist(Mockito.any(Airline.class));
        Assertions.assertDoesNotThrow(() ->
                airlineService.createAirline(airlineCreateRepresentation));
    }

    @Test
    void testCreateAirlineThrowsInvalidRequestException(){
        Mockito.when(airlineRepository.findOptionalAirlineByName("airline 1"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.of(airline));
        Assertions.assertThrows(InvalidRequestException.class,
                () -> airlineService.createAirline(airlineCreateRepresentation));
    }

    @Test
    void testUpdateAirlineDetails(){
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airline));
        Mockito.when(airlineRepository.findOptionalAirlineByName("airline 2"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.getEntityManager())
                .thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(Airline.class)))
                .thenReturn(airline);
        Assertions.assertDoesNotThrow(() ->
                airlineService.updateAirlineDetails(airlineUpdateRepresentation));
    }

    @Test
    void testUpdateAirlineDetailsThrowsResourceNotFoundException(){
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,() ->
                airlineService.updateAirlineDetails(airlineUpdateRepresentation));
    }

    @Test
    void testUpdateAirlineDetailsThrowsInvalidRequestException(){
        Airline otherAirline = AirlineUtil.createAirline();
        otherAirline.setAirlineName("airline 2");
        otherAirline.setU2digitCode("OA");
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airline));
        Mockito.when(airlineRepository.findOptionalAirlineByName("airline 2"))
                .thenReturn(Optional.of(otherAirline));
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidRequestException.class,() ->
                airlineService.updateAirlineDetails(airlineUpdateRepresentation));
    }

    @Test
    void testDeleteAirline(){
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.of(airline));
        Mockito.doNothing().when(airlineRepository).delete(airline);
        Assertions.assertDoesNotThrow(() -> airlineService.deleteAirline(1));
    }

    @Test
    void testDeleteAirlineDetailsThrowsResourceNotFoundException(){
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,() ->
                airlineService.deleteAirline(1));
    }
}
