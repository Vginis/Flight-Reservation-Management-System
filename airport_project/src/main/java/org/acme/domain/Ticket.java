package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Luggage> luggages = new ArrayList<>();

    @Embedded
    private FlightInformation flightInformation;

    @Embedded
    private PassengerInfo passengerInfo = new PassengerInfo();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightId", nullable = false)
    private Flight flight = new Flight();

    public Ticket() {
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getFirstName() {
        return passengerInfo.getFirstName();
    }

    public void setFirstName(String firstName) {
        this.passengerInfo.setFirstName(firstName);
    }

    public String getLastName() {
        return passengerInfo.getLastName();
    }

    public void setLastName(String lastName) {
        this.passengerInfo.setLastName(lastName);
    }

    public String getPassportId() {
        return passengerInfo.getPassportId();
    }

    public void setPassportId(String passportId) {
        this.passengerInfo.setPassportId(passportId);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

}
