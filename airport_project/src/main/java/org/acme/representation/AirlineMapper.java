package org.acme.representation;

import org.acme.domain.Airline;
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import jakarta.inject.Inject;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {FlightMapper.class})
public abstract class AirlineMapper {

    @Inject
    FlightRepository flightRepository;

    public abstract AirlineRepresentation toRepresentation(Airline airline);

    public abstract List<AirlineRepresentation> toRepresentationList(List<Airline> airline);

    @Mapping(target = "flights", ignore = true, dateFormat = "yyyyMMddHHmmss")
    public abstract Airline toModel(AirlineRepresentation representation);

    /*
    @AfterMapping
    protected void connectToFlight(AirlineRepresentation representation,
                                     @MappingTarget Airline airline) {

        if (representation.flights != null && representation.flights.id != null) {
            Flight flight = flightRepository.findById(representation.flights.id);
            if (flight == null) {
                throw new RuntimeException();
            }
            airline.setFlights(flight);
        }
    }
    */


   /* public Airline tomodel(AirlineRepresentation representation){
        if ( representation == null ) {
            return null;
        }

        Airline airline = new Airline();
        airline.setId(representation.id);
        airline.setUsername(representation.username);
        airline.setPassword(representation.password);
        airline.setAirlineName(representation.airlineName);
        airline.setU2digitCode(representation.u2digitCode);

        return airline;
    }*/

}
