package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import org.acme.domain.Reservation;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class PassengerRepresentation {
    public String username;
    public String password;
    public Integer id;
    public String email;
    public String phoneNumber;
    public String passportId;
    public List<ReservationRepresentation> reservations;

    //Method that turns ReservationRepresentation to Reservation, used for toModel implementation in PassengerMapper
    @Inject
    ReservationMapper reservationMapper;
    public List<Reservation> RepresentationToReservation(){
        ArrayList<Reservation> list = new ArrayList<Reservation>();

        for (ReservationRepresentation i : reservations)
            list.add(reservationMapper.toModel(i));
        return list;
    }
}
