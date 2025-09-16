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
public abstract class AirlineMapper {
    @Mapping(target = "flights", ignore = true)
    public abstract AirlineRepresentation toRepresentation(Airline airline);

    public abstract List<AirlineRepresentation> toRepresentationList(List<Airline> airline);

    @Mapping(target = "flights", ignore = true)
    @Mapping(target = "administrators", ignore = true)
    public abstract Airline toModel(AirlineRepresentation representation);

}
