package org.acme.representation;

import org.acme.domain.Passenger;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ReservationMapper.class})
public abstract class PassengerMapper {

    public abstract PassengerRepresentation toRepresentation(Passenger passenger);

    public abstract List<PassengerRepresentation> toRepresentationList(List<Passenger> passengers);

    @Mapping(target = "reservations", ignore = true)
    public abstract Passenger toModel(PassengerRepresentation representation);

    /* TODO θα πρέπει να βάλεις @Mapping για να σου τα εμφανίζει...δεν χρειάζεται να την υλοποιήσεις εσύ
    @Inject
    ReservationMapper reservationMapper;

    public PassengerRepresentation toRepresentation(Passenger passenger) {
        if (passenger == null) {
            return null;
        }

        PassengerRepresentation passengerRepresentation = new PassengerRepresentation();

        passengerRepresentation.username = passenger.getUsername();
        passengerRepresentation.password = passenger.getPassword();
        passengerRepresentation.id = passenger.getId();
        passengerRepresentation.email = passenger.getEmail();
        passengerRepresentation.phoneNumber = passenger.getPhoneNum();
        passengerRepresentation.passportId = passenger.getPassport_id();
        List<Reservation> list = passenger.getReservations();
        if (list != null) {
            List<ReservationRepresentation> list1 = new ArrayList<>();
            for (Reservation r : list){
                ReservationRepresentation reservationRepresentation = reservationMapper.toRepresentation(r);
                list1.add(reservationRepresentation);
            }

            passengerRepresentation.reservations=list1;
        }

        return passengerRepresentation;

    }

    @Mapping(target = "reservations", ignore = true)
    public abstract List<PassengerRepresentation> toRepresentationList(List<Passenger> passengers);

    public Passenger toModel(PassengerRepresentation representation){
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
        passenger.setReservations(representation.RepresentationToReservation());
        return passenger;

    }*/
}
