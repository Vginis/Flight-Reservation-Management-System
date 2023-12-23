package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import org.acme.domain.Reservation;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class PassengerRepresentation {
    public Integer id;
    public String username;
    public String password;
    public String email;
    public String phoneNum;
    public String passport_id;
    public List<ReservationRepresentation> reservations;

    //Method that turns ReservationRepresentation to Reservation, used for toModel implementation in PassengerMapper
    /*@Inject
    ReservationMapper reservationMapper;
    public List<Reservation> RepresentationToReservation(){
        ArrayList<Reservation> list = new ArrayList<Reservation>();

        for (ReservationRepresentation i : reservations)
            list.add(reservationMapper.toModel(i));
        return list;
    }*/
}
