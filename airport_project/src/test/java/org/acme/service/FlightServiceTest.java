package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.FlightStatus;
import org.acme.constant.SeatReservationState;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.Flight;
import org.acme.domain.FlightSeat;
import org.acme.domain.FlightSeatLayout;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.mapper.FlightMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.FlightRepository;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.representation.flight.FlightRepresentation;
import org.acme.representation.reservation.FlightSeatLayoutRepresentation;
import org.acme.representation.reservation.FlightSeatLayoutUpdateRepresentation;
import org.acme.search.FlightPageQuery;
import org.acme.search.PageResult;
import org.acme.util.AircraftUtil;
import org.acme.util.AirlineUtil;
import org.acme.util.AirportUtil;
import org.acme.util.FlightUtil;
import org.acme.util.UserContext;
import org.acme.util.UserUtil;
import org.acme.validation.FlightValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @Mock
    UserContext userContext;
    @Mock
    AirlineAdministratorRepository airlineAdministratorRepository;
    @Mock
    FlightPageQuery query;
    @Mock
    AircraftMapper aircraftMapper;

    FlightRepresentation flightRepresentation;
    Flight flight;
    FlightCreateRepresentation flightCreateRepresentation;
    AirlineAdministrator airlineAdministrator;
    Airline airline;
    Aircraft aircraft;
    FlightSeatLayout seatLayout;

    @BeforeEach
    void setup(){
        flightRepresentation = FlightUtil.createFlightRepresentation();
        flight = FlightUtil.createFlight();
        flightCreateRepresentation = FlightUtil.createFlightCreateRepresentation();
        airline = AirlineUtil.createAirline();
        airlineAdministrator = UserUtil.createAirlineAdministrator(airline);
        aircraft = AircraftUtil.createAircraft(airline);
        seatLayout = new FlightSeatLayout(aircraft);
    }

    @Test
    void testSearchFlightsByParams(){
        PageResult<Flight> pageResultFlight = new PageResult<>(1, List.of(flight));

        when(flightRepository.searchFlightsByParams(query))
                .thenReturn(pageResultFlight);
        when(flightMapper.map(flight))
                .thenReturn(flightRepresentation);
        when(aircraftRepository.findByIdOptional(any())).thenReturn(Optional.of(aircraft));
        PageResult<FlightRepresentation> representationPageResult = flightService.searchFlightsByParams(query);
        Assertions.assertEquals(1, representationPageResult.getTotal());
    }

    @Test
    void testSearchFlightsByParamsThrowsNotFoundException(){
        PageResult<Flight> pageResultFlight = new PageResult<>(1, List.of(flight));

        when(flightRepository.searchFlightsByParams(query))
                .thenReturn(pageResultFlight);
        when(flightMapper.map(flight))
                .thenReturn(flightRepresentation);
        when(aircraftRepository.findByIdOptional(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.searchFlightsByParams(query));
    }

    @Test
    void getFlightLayoutByUUID_shouldReturnLayout_whenFlightAndAircraftExist() {
        String uuid = "flight-uuid";

        when(flightRepository.getFlightByUUID(uuid)).thenReturn(Optional.of(flight));
        when(aircraftRepository.findByIdOptional(null)).thenReturn(Optional.of(aircraft));
        when(flightMapper.map(flight)).thenReturn(flightRepresentation);

        FlightSeatLayoutRepresentation result =
                flightService.getFlightLayoutByUUID(uuid);

        assertNotNull(result);
        verify(flightRepository).getFlightByUUID(uuid);
        verify(aircraftRepository).findByIdOptional(null);
    }

    @Test
    void getFlightLayoutByUUID_shouldThrow_whenFlightNotFound() {
        when(flightRepository.getFlightByUUID("missing"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> flightService.getFlightLayoutByUUID("missing"));
    }

    @Test
    void getFlightLayoutByUUID_shouldThrow_whenAircraftNotFound() {
        when(flightRepository.getFlightByUUID("uuid"))
                .thenReturn(Optional.of(flight));
        when(aircraftRepository.findByIdOptional(null))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> flightService.getFlightLayoutByUUID("uuid"));
    }

    @Test
    void updateSeatState_shouldUpdateSeat_whenValid() {
        FlightSeatLayoutUpdateRepresentation dto =
                new FlightSeatLayoutUpdateRepresentation();
        dto.setFlightUUID("uuid");
        dto.setRowIndex(1);
        dto.setColumnIndex(3);
        dto.setSeatReservationState(SeatReservationState.LOCKED);

        when(flightRepository.getFlightByUUID("uuid"))
                .thenReturn(Optional.of(flight));

        Assertions.assertDoesNotThrow(() -> flightService.updateSeatState(dto, "user1"));
    }

    @Test
    void updateSeatState_shouldThrow_whenFlightNotFound() {
        FlightSeatLayoutUpdateRepresentation dto =
                new FlightSeatLayoutUpdateRepresentation();
        dto.setFlightUUID("missing");

        when(flightRepository.getFlightByUUID("missing"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateSeatState(dto, "user"));
    }

    @Test
    void updateSeatState_shouldThrow_whenSeatNotFound() {
        FlightSeatLayoutUpdateRepresentation dto =
                new FlightSeatLayoutUpdateRepresentation();
        dto.setFlightUUID("uuid");
        dto.setRowIndex(99);
        dto.setColumnIndex(3);

        when(flightRepository.getFlightByUUID("uuid"))
                .thenReturn(Optional.of(flight));
        assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateSeatState(dto, "user"));
    }

    @Test
    void updateSeatState_shouldThrow_whenSeatIsBooked() {
        FlightSeatLayoutUpdateRepresentation dto =
                new FlightSeatLayoutUpdateRepresentation();
        dto.setFlightUUID("uuid");
        dto.setRowIndex(1);
        dto.setColumnIndex(1);
        dto.setSeatReservationState(SeatReservationState.LOCKED);

        flight.getFlightSeatLayout().getFlightSeats().clear();
        flight.getFlightSeatLayout().getFlightSeats().add(new FlightSeat(
                1,1,SeatReservationState.BOOKED, null, seatLayout
        ));
        when(flightRepository.getFlightByUUID("uuid"))
                .thenReturn(Optional.of(flight));
        assertThrows(InvalidRequestException.class,
                () -> flightService.updateSeatState(dto, "user"));
    }

    @Test
    void searchFlightsByMultipleParams_shouldValidateAndMap() {
        FlightMultipleParamsSearchDTO dto = new FlightMultipleParamsSearchDTO();
        PageResult<Flight> flightPage = new PageResult<>(1, List.of(flight));
        when(flightRepository.searchFlightsByMultipleParams(dto, 10, 0))
                .thenReturn(flightPage);
        when(flightMapper.map(flightPage))
                .thenReturn(new PageResult<>(0, List.of()));


        Assertions.assertDoesNotThrow(() -> flightService.searchFlightsByMultipleParams(dto, 10, 0));

        verify(flightValidator).validateSearchByMultipleParams(dto);
        verify(flightRepository).searchFlightsByMultipleParams(dto, 10, 0);
        verify(flightMapper).map(flightPage);
    }

    @Test
    void testCreateFlight(){
        when(aircraftRepository.findByIdOptional(1))
                 .thenReturn(Optional.of(AircraftUtil.createAircraft()));
        when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(AirportUtil.createAirport("SKG")));
        when(airportRepository.findAirportBy3DCode("SKG"))
                .thenReturn(Optional.of(AirportUtil.createAirport("ATH")));
        when(userContext.extractUsername()).thenReturn("username");
        when(airlineAdministratorRepository.findByUsername("username"))
                .thenReturn(Optional.ofNullable(airlineAdministrator));
        doNothing().when(flightValidator)
                .validateCreatedFlightDates(any(), any());
        doNothing().when(flightRepository)
                .persist(any(Flight.class));
        Assertions.assertDoesNotThrow(() -> flightService.createFlight(flightCreateRepresentation));
    }

    @Test
    void testCreateFlightResourceNotFoundException(){
        when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(AircraftUtil.createAircraft()));
        when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(AirportUtil.createAirport()));
        when(airportRepository.findAirportBy3DCode("SKG"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                flightService.createFlight(flightCreateRepresentation));
    }

    @Test
    void testCreateFlightInvalidRequestException(){
        when(aircraftRepository.findByIdOptional(1))
                .thenReturn(Optional.of(AircraftUtil.createAircraft()));
        when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(AirportUtil.createAirport("SKG")));
        when(airportRepository.findAirportBy3DCode("SKG"))
                .thenReturn(Optional.of(AirportUtil.createAirport("SKG")));
        when(userContext.extractUsername()).thenReturn("username");
        when(airlineAdministratorRepository.findByUsername("username"))
                .thenReturn(Optional.ofNullable(airlineAdministrator));
        doNothing().when(flightValidator)
                .validateCreatedFlightDates(any(), any());

        assertThrows(InvalidRequestException.class, () -> flightService.createFlight(flightCreateRepresentation));
    }

    @Test
    void testUpdateFlightDates(){
        FlightDateUpdateRepresentation flightDateUpdateRepresentation = FlightUtil.createFlightDateUpdateRepresentation();
        when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.of(flight));
        doNothing().when(flightValidator)
                .validateCreatedFlightDates(flightDateUpdateRepresentation.getDepartureTime()
                        , flightDateUpdateRepresentation.getArrivalTime());
        when(flightRepository.getEntityManager())
                .thenReturn(entityManager);
        when(entityManager.merge(any(Flight.class)))
                .thenReturn(flight);
        Assertions.assertDoesNotThrow(() -> flightService.updateFlightDates(flightDateUpdateRepresentation, 1));
    }

    @Test
    void testUpdateFlightDatesThrowsResourceNotFoundException(){
        FlightDateUpdateRepresentation flightDateUpdateRepresentation = FlightUtil.createFlightDateUpdateRepresentation();
        when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateFlightDates(flightDateUpdateRepresentation, 1));
    }

    @Test
    void testCancelFlight(){
        when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.of(flight));
        Assertions.assertDoesNotThrow(
                () -> flightService.updateFlightStatus(1, FlightStatus.CANCELLED));
    }

    @Test
    void testCancelFlightThrowsResourceNotFoundException(){
        when(flightRepository.findByIdOptional(1))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> flightService.updateFlightStatus(1,FlightStatus.CANCELLED));
    }
}
