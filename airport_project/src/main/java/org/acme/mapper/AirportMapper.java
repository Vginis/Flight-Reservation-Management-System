package org.acme.mapper;

import jakarta.inject.Inject;
import org.acme.domain.Airport;
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import org.acme.representation.AirportRepresentation;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class})
public abstract class AirportMapper {

    @Inject
    FlightRepository flightRepository;

    @Mapping(target = "depFlights", expression = "java(airport.getDepFlights().stream().map(org.acme.domain.Flight::getId).collect(Collectors.toList()))")
    @Mapping(target = "arrFlights", expression = "java(airport.getArrFlights().stream().map(org.acme.domain.Flight::getId).collect(Collectors.toList()))")
    public abstract AirportRepresentation toRepresentation(Airport airport);

    public abstract List<AirportRepresentation> toRepresentationList(List<Airport> airport);

    @Mapping(target = "depFlights", ignore = true)
    @Mapping(target = "arrFlights", ignore = true)
    public abstract Airport toModel(AirportRepresentation representation);

    @AfterMapping
    public void resolveDepFlightsById(AirportRepresentation dto, @MappingTarget Airport airport) {
        List<Flight> depFlights = new ArrayList<>(dto.depFlights.size());
        Flight flight = null;
        for (Integer df : dto.depFlights) {
            if (df != null) {
                flight = flightRepository.find("id", df).firstResultOptional().orElse(null);
            }
            depFlights.add(flight);
        }
        airport.setDepFlights(depFlights);
    }

    @AfterMapping
    public void resolveArrFlightsById(AirportRepresentation dto, @MappingTarget Airport airport) {
        List<Flight> arrFlights = new ArrayList<>(dto.arrFlights.size());
        Flight flight = null;
        for (Integer af : dto.arrFlights) {
            if (af != null) {
                flight = flightRepository.find("id", af).firstResultOptional().orElse(null);
            }
            arrFlights.add(flight);
        }
        airport.setArrFlights(arrFlights);
    }

}
