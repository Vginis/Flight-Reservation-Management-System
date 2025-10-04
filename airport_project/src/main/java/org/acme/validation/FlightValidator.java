package org.acme.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.constant.ErrorMessages;
import org.acme.domain.Airport;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.AirportRepository;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ApplicationScoped
public class FlightValidator {
    @Inject
    AirportRepository airportRepository;

    public void validateCreatedFlightDates(LocalDateTime departureTime, LocalDateTime arrivalTime){
        if(departureTime.isBefore(LocalDateTime.now().plusMonths(6))){
            throw new InvalidRequestException("Departure time must be at least 6 months old.");
        }

        if (departureTime.isAfter(arrivalTime)) {
            throw new InvalidRequestException("Arrival time cannot be earlier than the departure.");
        }
    }

    public void validateSearchByMultipleParams(FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO){
        String departureAirport = flightMultipleParamsSearchDTO.getDepartureAirport();
        String arrivalAirport = flightMultipleParamsSearchDTO.getArrivalAirport();
        String departureDate = flightMultipleParamsSearchDTO.getDepartureDate();
        String arrivalDate = flightMultipleParamsSearchDTO.getArrivalDate();
        if(departureAirport!=null && departureAirport.equals(arrivalAirport)){
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }

        if(departureAirport!=null){
            checkAirportExistence(departureAirport);
        }

        if(arrivalAirport!=null){
            checkAirportExistence(arrivalAirport);
        }

        validateSearchByMultipleParamsForDates(departureDate, arrivalDate);
    }

    private void validateSearchByMultipleParamsForDates(String departureDateString, String arrivalDateString){
        if(dateIsBeforeCurrentDay(departureDateString) || dateIsBeforeCurrentDay(arrivalDateString) || dateIsBeforeSpecifiedDay(departureDateString, arrivalDateString)){
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }
    }

    private boolean dateIsBeforeCurrentDay(String dateString){
        if(dateString==null) return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.isBefore(LocalDate.now());
    }

    private boolean dateIsBeforeSpecifiedDay(String departureDateString, String arrivalDateString){
        if(departureDateString==null || arrivalDateString==null) return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate departureDate = LocalDate.parse(departureDateString, formatter);
        LocalDate arrivalDate = LocalDate.parse(arrivalDateString, formatter);
        return arrivalDate.isBefore(departureDate);
    }

    private void checkAirportExistence(String airport){
        Optional<Airport> departureAirportOptional = airportRepository.findAirportBy3DCode(airport);
        if(departureAirportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
    }
}
