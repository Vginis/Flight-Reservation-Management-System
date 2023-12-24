package org.acme.representation;

import org.acme.domain.Flight;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class})
public abstract class FlightMapper {

    @Mapping(target = "airlineName", source = "airline.airlineName")
    @Mapping(target = "departureAirport", source = "departureAirport.airportName")
    @Mapping(target = "arrivalAirport", source = "arrivalAirport.airportName")
    public abstract FlightRepresentation toRepresentation(Flight flight);

    public abstract List<FlightRepresentation> toRepresentationList(List<Flight> flights);

    @Mapping(target = "airline.airlineName", source = "airlineName")
    @Mapping(target = "departureAirport.airportName", source = "departureAirport")
    @Mapping(target = "arrivalAirport.airportName", source = "arrivalAirport")
    @Mapping(target = "ticketList", ignore = true)
    public abstract Flight toModel(FlightRepresentation representation);

}
