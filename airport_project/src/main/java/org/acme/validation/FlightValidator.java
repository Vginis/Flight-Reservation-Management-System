package org.acme.validation;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.exception.InvalidRequestException;

import java.time.LocalDateTime;

@ApplicationScoped
public class FlightValidator {
    public void validateCreatedFlightDates(LocalDateTime departureTime, LocalDateTime arrivalTime){
        if(departureTime.isBefore(LocalDateTime.now().plusMonths(6))){
            throw new InvalidRequestException("Departure time must be at least 6 months old.");
        }

        if (departureTime.isAfter(arrivalTime)) {
            throw new InvalidRequestException("Arrival time cannot be earlier than the departure.");
        }
    }
}
