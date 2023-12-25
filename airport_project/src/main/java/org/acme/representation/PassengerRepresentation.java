package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class PassengerRepresentation {
    public Integer id;
    public String username;
    public String password;
    public String email;
    public String phoneNum;
    public String passport_id;
    public List<Integer> reservationsId;

    /* TODO κοίτα μια τη ReservationMapper -> γραμμές 22 & 23
    Method that turns ReservationRepresentation to Reservation, used for toModel implementation in PassengerMapper
    @Inject
    ReservationMapper reservationMapper;
    public List<Reservation> RepresentationToReservation(){
        ArrayList<Reservation> list = new ArrayList<Reservation>();

        for (ReservationRepresentation i : reservations)
            list.add(reservationMapper.toModel(i));
        return list;
    }*/
}
