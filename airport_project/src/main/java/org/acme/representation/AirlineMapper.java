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

    @Mapping(target = "flights", ignore = true, dateFormat = "yyyyMMddHHmmss")
    public abstract Airline toModel(AirlineRepresentation representation);

    /*public Airline toModel(AirlineRepresentation representation){
        if ( representation == null ) {
            return null;
        }

        Airline airline = new Airline();
        airline.setId(representation.id);
        airline.setUsername(representation.username);
        airline.setPassword(representation.password);
        airline.setName( representation.name );
        airline.setU2digitCode( representation.u2digitCode );

        return airline;
    }*/

}
