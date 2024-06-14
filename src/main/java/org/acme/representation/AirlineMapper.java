package org.acme.representation;

import org.acme.domain.Airline;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {FlightMapper.class})
public abstract class AirlineMapper {

    public abstract AirlineRepresentation toRepresentation(Airline airline);

    public abstract List<AirlineRepresentation> toRepresentationList(List<Airline> airline);

    @Mapping(target = "flights", dateFormat = "yyyyMMddHHmmss")
    public abstract Airline toModel(AirlineRepresentation representation);

}
