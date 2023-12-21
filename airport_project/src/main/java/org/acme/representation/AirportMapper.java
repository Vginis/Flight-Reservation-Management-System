package org.acme.representation;

import java.util.List;

import org.acme.domain.Airport;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirportMapper {
    public abstract AirportRepresentation toRepresentation(Airport airport);

    public abstract List<AirportRepresentation> toRepresentationList(List<Airport> airport);

    public abstract Airport toModel(AirportRepresentation representation);
}