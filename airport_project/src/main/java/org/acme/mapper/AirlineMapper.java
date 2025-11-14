package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airline;
import org.acme.representation.airline.AirlineRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {FlightMapper.class})
@RequestScoped
public interface AirlineMapper extends OneWayMapper<AirlineRepresentation, Airline> {
    @Override
    @Mapping(target = "fileRepresentation", ignore = true)
    AirlineRepresentation map(Airline e);
}
