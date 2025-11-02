package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Flight;
import org.acme.representation.flight.FlightRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class},
        uses = { AirportMapper.class }
)
@RequestScoped
public interface FlightMapper extends OneWayMapper<FlightRepresentation, Flight> {
    @Override
    @Mapping(target = "airlineU2DigitCode", expression = "java(e.getAirline().getU2digitCode())")
    @Mapping(target = "aircraft", ignore = true)
    FlightRepresentation map(Flight e);
}
