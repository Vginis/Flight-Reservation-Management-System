package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.domain.*;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.FlightMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.FlightRepository;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.representation.flight.FlightRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.validation.FlightValidator;

import java.util.Optional;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightMapper flightMapper;
    @Inject
    FlightRepository flightRepository;
    @Inject
    AirlineRepository airlineRepository;
    @Inject
    AircraftRepository aircraftRepository;
    @Inject
    AirportRepository airportRepository;
    @Inject
    FlightValidator flightValidator;

    public PageResult<FlightRepresentation> searchFlightsByParams(PageQuery<FlightSortAndFilterBy> query){
        PageResult<Flight> flightPageResult = flightRepository.searchFlightsByParams(query);
        return flightMapper.map(flightPageResult);
    }

    public PageResult<FlightRepresentation> searchFlightsByMultipleParams(FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO, Integer size, Integer index){
        flightValidator.validateSearchByMultipleParams(flightMultipleParamsSearchDTO);
        PageResult<Flight> flightPageResult = flightRepository.searchFlightsByMultipleParams(flightMultipleParamsSearchDTO, size, index);
        return flightMapper.map(flightPageResult);
    }

    @Transactional
    public void createFlight(FlightCreateRepresentation flightCreateRepresentation){
        Optional<Airline> airlineOptional = airlineRepository.findByIdOptional(flightCreateRepresentation.getAirlineId());
        Optional<Aircraft> aircraftOptional = aircraftRepository.findByIdOptional(flightCreateRepresentation.getAircraftId());
        Optional<Airport> departureAirportOptional = airportRepository.findAirportBy3DCode(flightCreateRepresentation.getDepartureAirport());
        Optional<Airport> arrivalAirportOptional = airportRepository.findAirportBy3DCode(flightCreateRepresentation.getArrivalAirport());

        if((airlineOptional.isEmpty() || aircraftOptional.isEmpty() || departureAirportOptional.isEmpty() || arrivalAirportOptional.isEmpty())){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        flightValidator.validateCreatedFlightDates(flightCreateRepresentation.getDepartureTime(),flightCreateRepresentation.getArrivalTime());

        Flight flight = new Flight(flightCreateRepresentation, airlineOptional.get(), departureAirportOptional.get(),arrivalAirportOptional.get(), new FlightSeatLayout());
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
    public void deleteFlight(Integer flightId) {
        Optional<Flight> flightOptional = flightRepository.findByIdOptional(flightId);
        if(flightOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Flight flight = flightOptional.get();
        flightRepository.delete(flight);
    }
}
