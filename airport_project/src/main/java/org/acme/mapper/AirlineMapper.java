package org.acme.mapper;

import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airline;
import org.acme.representation.AirlineRepresentation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {FlightMapper.class})
@RequestScoped
public abstract class AirlineMapper {

    public abstract AirlineRepresentation toRepresentation(Airline airline);

    public abstract List<AirlineRepresentation> toRepresentationList(List<Airline> airline);

    @Mapping(target = "flights", ignore = true)
    @Mapping(target = "administrators", ignore = true)
    public abstract Airline toModel(AirlineRepresentation representation);

}
