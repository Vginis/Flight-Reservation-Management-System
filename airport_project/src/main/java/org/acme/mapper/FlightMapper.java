package org.acme.mapper;

import org.acme.domain.*;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.TicketRepository;
import org.acme.representation.FlightRepresentation;
import org.mapstruct.*;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class},
        imports = {Collectors.class})
public abstract class FlightMapper {

    @Inject
    AirportRepository airportRepository;

    @Inject
    AirlineRepository airlineRepository;

    @Inject
    TicketRepository ticketRepository;

    @Mapping(target = "airlineName", source = "airline.airlineName")
    @Mapping(target = "departureAirport", source = "departureAirport.airportName")
    @Mapping(target = "arrivalAirport", source = "arrivalAirport.airportName")
    public abstract FlightRepresentation toRepresentation(Flight flight);

    public abstract List<FlightRepresentation> toRepresentationList(List<Flight> flights);

//    @Mapping(target = "airline", ignore = true)
//    @Mapping(target = "departureAirport", ignore = true)
//    @Mapping(target = "arrivalAirport", ignore = true)
//    @Mapping(target = "ticketList", ignore = true)
//    public abstract Flight toModel(FlightRepresentation representation);

}
