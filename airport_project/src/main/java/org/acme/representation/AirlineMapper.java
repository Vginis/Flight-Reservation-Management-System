package org.acme.representation;

import org.acme.domain.Airline;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AirlineMapper {

    public abstract AirlineRepresentation toRepresentation(Airline airline);

    public abstract List<AirlineRepresentation> toRepresentationList(List<Airline> airline);

    public Airline toModel(AirlineRepresentation representation){
        if ( representation == null ) {
            return null;
        }

        Airline airline = new Airline();
        airline.setUsername(representation.username);
        airline.setPassword(representation.password);
        airline.setName( representation.name );
        airline.setU2digitCode( representation.u2digitCode );
        airline.setId(representation.id);
        return airline;
    }

}
