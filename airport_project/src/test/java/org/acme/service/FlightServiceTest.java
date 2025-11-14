package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.FlightStatus;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Flight;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.FlightMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.FlightRepository;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightRepresentation;
import org.acme.search.FlightPageQuery;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.AircraftUtil;
import org.acme.util.AirlineUtil;
import org.acme.util.AirportUtil;
import org.acme.util.FlightUtil;
import org.acme.validation.FlightValidator;
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
class FlightServiceTest {
    @InjectMocks
    FlightService flightService;
    @Mock
    FlightMapper flightMapper;
    @Mock
    FlightRepository flightRepository;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    AirportRepository airportRepository;
    @Mock
    AircraftRepository aircraftRepository;
    @Mock
    FlightValidator flightValidator;
    @Mock
    EntityManager entityManager;

    FlightRepresentation flightRepresentation;
    Flight flight;
    FlightCreateRepresentation flightCreateRepresentation;
    @BeforeEach
    void setup(){
        flightRepresentation = FlightUtil.createFlightRepresentation();
        flight = FlightUtil.createFlight();
        flightCreateRepresentation = FlightUtil.createFlightCreateRepresentation();
    }

//    @Test
//    void testSearchFlightsByParams(){
//        FlightPageQuery query = new FlightPageQuery();
//                FlightSortAndFilterBy.FLIGHT_UUID, "uuid", 5, 0,
//                FlightSortAndFilterBy.FLIGHT_UUID, SortDirection.DESCENDING
//        );
//        PageResult<Flight> pageResultFlight = new PageResult<>(1, List.of(flight));
//
//        Mockito.when(flightRepository.searchFlightsByParams(query))
//                .thenReturn(pageResultFlight);
//        Mockito.when(flightMapper.map(pageResultFlight))
//                .thenReturn(new PageResult<>(1, List.of(flightRepresentation)));
//        PageResult<FlightRepresentation> representationPageResult = flightService.searchFlightsByParams(query);
//        Assertions.assertEquals(1, representationPageResult.getTotal());
//    }

    @Test
    void testCreateFlight(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                 .thenReturn(Optional.of(AircraftUtil.createAircraft()));
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.of(AirlineUtil.createAirline()));
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(AirportUtil.createAirport()));
        Mockito.when(airportRepository.findAirportBy3DCode("SKG"))
                .thenReturn(Optional.of(AirportUtil.createAirport()));
        Mockito.doNothing().when(flightValidator)
                .validateCreatedFlightDates(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(flightRepository)
                .persist(Mockito.any(Flight.class));
        Assertions.assertDoesNotThrow(() -> flightService.createFlight(flightCreateRepresentation));
    }

    @Test
    void testCreateFlightResourceNotFoundException(){
        Mockito.when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(AircraftUtil.createAircraft()));
        Mockito.when(airlineRepository.findByIdOptional(1))
                .thenReturn(Optional.of(AirlineUtil.createAirline()));
        Mockito.when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(AirportUtil.createAirport()));
        Mockito.when(airportRepository.findAirportBy3DCode("SKG"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                flightService.createFlight(flightCreateRepresentation));
    }

    @Test
    void testUpdateFlightDates(){
        FlightDateUpdateRepresentation flightDateUpdateRepresentation = FlightUtil.createFlightDateUpdateRepresentation();
        Mockito.when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.of(flight));
        Mockito.doNothing().when(flightValidator)
                .validateCreatedFlightDates(flightDateUpdateRepresentation.getDepartureTime()
                        , flightDateUpdateRepresentation.getArrivalTime());
        Mockito.when(flightRepository.getEntityManager())
                .thenReturn(entityManager);
        Mockito.when(entityManager.merge(Mockito.any(Flight.class)))
                .thenReturn(flight);
        Assertions.assertDoesNotThrow(() -> flightService.updateFlightDates(flightDateUpdateRepresentation, 1));
    }

    @Test
    void testUpdateFlightDatesThrowsResourceNotFoundException(){
        FlightDateUpdateRepresentation flightDateUpdateRepresentation = FlightUtil.createFlightDateUpdateRepresentation();
        Mockito.when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateFlightDates(flightDateUpdateRepresentation, 1));
    }

    @Test
    void testCancelFlight(){
        Mockito.when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.of(flight));
        Mockito.doNothing().when(flightRepository)
                        .delete(Mockito.any(Flight.class));
        Assertions.assertDoesNotThrow(
                () -> flightService.updateFlightStatus(1, FlightStatus.CANCELLED));
    }

    @Test
    void testCancelFlightThrowsResourceNotFoundException(){
        Mockito.when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateFlightStatus(1,FlightStatus.CANCELLED));
    }
}
