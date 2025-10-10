package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airport;
import org.acme.representation.airport.AirportRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {Collectors.class})
@RequestScoped
public interface AirportMapper extends OneWayMapper<AirportRepresentation, Airport> {
    @Override
    @Mapping(target = "airportId", source = "id")
    AirportRepresentation map(Airport e);
}
