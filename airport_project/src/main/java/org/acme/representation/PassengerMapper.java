package org.acme.representation;

import jakarta.inject.Inject;
import org.acme.domain.Passenger;
import org.acme.persistence.ReservationRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PassengerMapper {

    @Inject
    ReservationRepository reservationRepository;

    public abstract PassengerRepresentation toRepresentation(Passenger passenger);

    public abstract List<PassengerRepresentation> toRepresentationList(List<Passenger> passengers);

    public  Passenger toModel(PassengerRepresentation representation){
        if ( representation == null ) {
            return null;
        }

        Passenger passenger = new Passenger();

        passenger.setId( representation.id );
        passenger.setUsername( representation.username );
        passenger.setPassword( representation.password );
        passenger.setEmail( representation.email );
        passenger.setPassport_id(representation.passportId);
        passenger.setPhoneNum(representation.phoneNumber);
        return passenger;
    };
}
