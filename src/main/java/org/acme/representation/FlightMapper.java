package org.acme.representation;

import org.acme.domain.*;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.TicketRepository;
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
    @Mapping(target = "ticketList", expression = "java(flight.getTicketList().stream().map(org.acme.domain.Ticket::getTicketId).collect(Collectors.toList()))")
    public abstract FlightRepresentation toRepresentation(Flight flight);

    public abstract List<FlightRepresentation> toRepresentationList(List<Flight> flights);

    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "departureAirport", ignore = true)
    @Mapping(target = "arrivalAirport", ignore = true)
    @Mapping(target = "ticketList", ignore = true)
    public abstract Flight toModel(FlightRepresentation representation);

    @AfterMapping
    public void resolveAirlineByName(FlightRepresentation dto, @MappingTarget Flight flight) {
        Airline airline = null;
        if (dto.airlineName != null){
            airline = airlineRepository.find("airlineName", dto.airlineName)
                    .firstResultOptional().orElse(null);
        }
        flight.setAirline(airline);
    }

    @AfterMapping
    public void resolveAirportByDepartureAirport(FlightRepresentation dto, @MappingTarget Flight flight) {
        Airport airport = null;
        if (dto.departureAirport != null){
            airport = airportRepository.find("airportName", dto.departureAirport)
                    .firstResultOptional().orElse(null);
        }
        flight.setDepartureAirport(airport);
    }

    @AfterMapping
    public void resolveAirportByArrivalAirport(FlightRepresentation dto, @MappingTarget Flight flight) {
        Airport airport = null;
        if (dto.arrivalAirport != null){
            airport = airportRepository.find("airportName", dto.arrivalAirport)
                    .firstResultOptional().orElse(null);
        }
        flight.setArrivalAirport(airport);
    }

    @AfterMapping
    public void resolveTicketListById(FlightRepresentation dto, @MappingTarget Flight flight) {
        List<Ticket> ticket_list = new ArrayList<>(dto.ticketList.size());
        Ticket ticket = null;
        for (Integer i : dto.ticketList) {
            if (i != null) {
                ticket = ticketRepository.find("ticketId", i).firstResultOptional().orElse(null);
            }
            ticket_list.add(ticket);
        }
        flight.setTicketList(ticket_list);
    }

}
