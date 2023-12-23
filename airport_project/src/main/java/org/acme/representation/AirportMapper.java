package org.acme.representation;

import org.acme.domain.Airport;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirportMapper {

    public abstract AirportRepresentation toRepresentation(Airport airport);

    public abstract List<AirportRepresentation> toRepresentationList(List<Airport> airport);

    public Airport toModel(AirportRepresentation representation) {
        if (representation == null) {
            return null;
        }

        Airport airport = new Airport();
        airport.setAirportId(representation.airportId);
        airport.setName(representation.name);
        airport.setCity(representation.city);
        airport.setCountry(representation.country);
        airport.setU3digitCode(representation.u3digitCode);
        return airport;
    }

}
