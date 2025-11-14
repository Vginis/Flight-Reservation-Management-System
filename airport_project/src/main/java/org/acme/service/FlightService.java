package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.FlightStatus;
import org.acme.domain.*;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.mapper.FlightMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.FlightRepository;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.representation.flight.FlightRepresentation;
import org.acme.search.FlightPageQuery;
import org.acme.search.PageResult;
import org.acme.util.UserContext;
import org.acme.validation.FlightValidator;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightMapper flightMapper;
    @Inject
    FlightRepository flightRepository;
    @Inject
    AircraftRepository aircraftRepository;
    @Inject
    AirportRepository airportRepository;
    @Inject
    FlightValidator flightValidator;
    @Inject
    UserContext userContext;
    @Inject
    AirlineAdministratorRepository airlineAdministratorRepository;
    @Inject
    AircraftMapper aircraftMapper;

    public PageResult<FlightRepresentation> searchFlightsByParams(FlightPageQuery query){
        PageResult<Flight> flightPageResult = flightRepository.searchFlightsByParams(query);
        List<FlightRepresentation> flightRepresentations = flightPageResult.getResults().stream().map(flight -> {
            FlightRepresentation flightRepresentation = flightMapper.map(flight);

            Optional<Aircraft> aircraftOptional = aircraftRepository.findByIdOptional(flight.getFlightSeatLayout().getAircraftId());
            if(aircraftOptional.isEmpty()){
                throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
            }
            flightRepresentation.setAircraft(aircraftMapper.map(aircraftOptional.get()));
            return flightRepresentation;
        }).toList();

        return new PageResult<>(flightPageResult.getTotal(), flightRepresentations);
    }

    public PageResult<FlightRepresentation> searchFlightsByMultipleParams(FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO, Integer size, Integer index){
        flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO);
        PageResult<Flight> flightPageResult = flightRepository.searchFlightsByMultipleParams(flightMultipleParamsSearchDTO, size, index);
        return flightMapper.map(flightPageResult);
    }

    @Transactional
    public void createFlight(FlightCreateRepresentation flightCreateRepresentation){
        String username = userContext.extractUsername();
        Optional<AirlineAdministrator> airlineAdministratorOptional = airlineAdministratorRepository.findByUsername(username);
        Optional<Aircraft> aircraftOptional = aircraftRepository.findByIdOptional(flightCreateRepresentation.getAircraftId());
        Optional<Airport> departureAirportOptional = airportRepository.findAirportBy3DCode(flightCreateRepresentation.getDepartureAirport());
        Optional<Airport> arrivalAirportOptional = airportRepository.findAirportBy3DCode(flightCreateRepresentation.getArrivalAirport());

        if((airlineAdministratorOptional.isEmpty() || aircraftOptional.isEmpty() || departureAirportOptional.isEmpty() || arrivalAirportOptional.isEmpty())){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        flightValidator.validateCreatedFlightDates(flightCreateRepresentation.getDepartureTime(),flightCreateRepresentation.getArrivalTime());
        Aircraft aircraft = aircraftOptional.get();
        Airport departureAirport = departureAirportOptional.get();
        Airport arrivalAirport = arrivalAirportOptional.get();
        Airline airline = airlineAdministratorOptional.get().getAirline();
        if (!airline.getU2digitCode().equals(aircraft.getAirline().getU2digitCode()) 
            || departureAirport.getU3digitCode().equals(arrivalAirport.getU3digitCode())) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }

        FlightSeatLayout flightSeatLayout = new FlightSeatLayout(aircraft);
        Flight flight = new Flight(flightCreateRepresentation, airline, departureAirport,arrivalAirport, flightSeatLayout);
        flightSeatLayout.setFlight(flight);

        flightRepository.persist(flight);
    }


    @Transactional
    public void updateFlightDates(FlightDateUpdateRepresentation flightUpdateRepresentation, Integer flightId){
        Optional<Flight> flightOptional = flightRepository.findByIdOptional(flightId);
        if(flightOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        flightValidator.validateCreatedFlightDates(flightUpdateRepresentation.getDepartureTime(), flightUpdateRepresentation.getArrivalTime());

        Flight flight = flightOptional.get();
        flight.editDates(flightUpdateRepresentation);
        flightRepository.getEntityManager().merge(flight);
    }

    @Transactional
    public void updateFlightStatus(Integer flightId, FlightStatus newFlightStatus) {
        Optional<Flight> flightOptional = flightRepository.findByIdOptional(flightId);
        if(flightOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Flight flight = flightOptional.get();
        FlightStatus currentFlightStatus = flight.getFlightStatus();
        flightValidator.validateFlightStatusUpdate(currentFlightStatus, newFlightStatus);
        flight.updateFlight(newFlightStatus);
    }
}
