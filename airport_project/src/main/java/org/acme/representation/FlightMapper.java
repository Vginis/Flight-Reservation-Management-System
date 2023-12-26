package org.acme.representation;

import org.acme.domain.Airport;
import org.acme.domain.Flight;
import org.acme.persistence.AirportRepository;
import org.mapstruct.*;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class},
        imports = {Collectors.class})
public abstract class FlightMapper {

    @Inject
    AirportRepository airportRepository;


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
    public void resolveAirportByDepartureAirport(FlightRepresentation dto, @MappingTarget Flight flight){

        Airport airport = null;
        if (dto.departureAirport != null){
            airport = airportRepository.find("airportName", dto.departureAirport)
                    .firstResultOptional().orElse(null);
        }
        flight.setDepartureAirport(airport);
    }

    @AfterMapping
    public void resolveAirportByArrivalAirport(FlightRepresentation dto, @MappingTarget Flight flight){

        Airport airport = null;
        if (dto.arrivalAirport != null){
            airport = airportRepository.find("airportName", dto.arrivalAirport)
                    .firstResultOptional().orElse(null);
        }
        flight.setArrivalAirport(airport);
    }
}
