package org.acme.validation;

import org.acme.constant.FlightStatus;
import org.acme.domain.Airport;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.AirportRepository;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightValidatorTest {

    @InjectMocks
    FlightValidator flightValidator;

    @Mock
    AirportRepository airportRepository;

    private FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO;

    @BeforeEach
    void setUp() {
        flightMultipleParamsSearchDTO = new FlightMultipleParamsSearchDTO();
    }

    @Test
    void validateCreatedFlightDates_shouldPass_whenDatesAreValid() {
        LocalDateTime departure = LocalDateTime.now().plusMonths(6).plusDays(1);
        LocalDateTime arrival = departure.plusHours(2);

        Assertions.assertDoesNotThrow(() ->
                flightValidator.validateCreatedFlightDates(departure, arrival)
        );
    }

    @Test
    void validateCreatedFlightDates_shouldThrow_whenDepartureTooEarly() {
        LocalDateTime departure = LocalDateTime.now().plusMonths(5);
        LocalDateTime arrival = departure.plusHours(2);

        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateCreatedFlightDates(departure, arrival)
        );
    }

    @Test
    void validateCreatedFlightDates_shouldThrow_whenArrivalBeforeDeparture() {
        LocalDateTime departure = LocalDateTime.now().plusMonths(6).plusDays(1);
        LocalDateTime arrival = departure.minusHours(1);

        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateCreatedFlightDates(departure, arrival)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldThrow_whenAirportsAreSame() {
        flightMultipleParamsSearchDTO.setDepartureAirport("ATH");
        flightMultipleParamsSearchDTO.setArrivalAirport("ATH");

        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldThrow_whenDepartureAirportNotFound() {
        flightMultipleParamsSearchDTO.setDepartureAirport("AAA");

        when(airportRepository.findAirportBy3DCode("AAA"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldThrow_whenArrivalAirportNotFound() {
        flightMultipleParamsSearchDTO.setArrivalAirport("BBB");

        when(airportRepository.findAirportBy3DCode("BBB"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldThrow_whenDepartureDateInPast() {
        flightMultipleParamsSearchDTO.setDepartureDate(LocalDate.now().minusDays(1).toString());

        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldThrow_whenArrivalBeforeDeparture() {
        flightMultipleParamsSearchDTO.setDepartureDate(LocalDate.now().plusDays(5).toString());
        flightMultipleParamsSearchDTO.setArrivalDate(LocalDate.now().plusDays(2).toString());

        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateSearchByMultipleParams_shouldPass_whenAllValuesValid() {
        flightMultipleParamsSearchDTO.setDepartureAirport("ATH");
        flightMultipleParamsSearchDTO.setArrivalAirport("LHR");
        flightMultipleParamsSearchDTO.setDepartureDate(LocalDate.now().plusDays(5).toString());
        flightMultipleParamsSearchDTO.setArrivalDate(LocalDate.now().plusDays(6).toString());

        when(airportRepository.findAirportBy3DCode("ATH"))
                .thenReturn(Optional.of(new Airport()));
        when(airportRepository.findAirportBy3DCode("LHR"))
                .thenReturn(Optional.of(new Airport()));

        Assertions.assertDoesNotThrow(() ->
                flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO)
        );
    }

    @Test
    void validateFlightStatusUpdate_shouldAllowInFlightToArrived() {
        Assertions.assertDoesNotThrow(() ->
                flightValidator.validateFlightStatusUpdate(
                        FlightStatus.IN_FLIGHT,
                        FlightStatus.ARRIVED
                )
        );
    }

    @Test
    void validateFlightStatusUpdate_shouldThrow_whenInFlightToInvalidStatus() {
        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateFlightStatusUpdate(
                        FlightStatus.IN_FLIGHT,
                        FlightStatus.CANCELLED
                )
        );
    }

    @Test
    void validateFlightStatusUpdate_shouldThrow_whenCurrentStatusIsCancelled() {
        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateFlightStatusUpdate(
                        FlightStatus.CANCELLED,
                        FlightStatus.IN_FLIGHT
                )
        );
    }

    @Test
    void validateFlightStatusUpdate_shouldAllowDelayedToCancelled() {
        Assertions.assertDoesNotThrow(() ->
                flightValidator.validateFlightStatusUpdate(
                        FlightStatus.DELAYED,
                        FlightStatus.CANCELLED
                )
        );
    }

    @Test
    void validateFlightStatusUpdate_shouldThrow_whenDelayedToArrived() {
        Assertions.assertThrows(
                InvalidRequestException.class,
                () -> flightValidator.validateFlightStatusUpdate(
                        FlightStatus.DELAYED,
                        FlightStatus.ARRIVED
                )
        );
    }
}
