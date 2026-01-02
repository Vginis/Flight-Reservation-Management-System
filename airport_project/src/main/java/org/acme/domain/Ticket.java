package org.acme.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "ticket_uuid", nullable = false)
    private String ticketUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Luggage> luggages;

    @Embedded
    private FlightInformation flightInformation;

    @Embedded
    private PassengerInfo passengerInfo;

    public Ticket() {
    }

    public Ticket(List<Luggage> luggages, FlightInformation flightInformation, PassengerInfo passengerInfo) {
        this.ticketUUID = UUID.randomUUID().toString();
        this.luggages = luggages;
        this.flightInformation = flightInformation;
        this.passengerInfo = passengerInfo;
    }

    public String getTicketUUID() {
        return ticketUUID;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Luggage> getLuggages() {
        return luggages;
    }

    public FlightInformation getFlightInformation() {
        return flightInformation;
    }

    public PassengerInfo getPassengerInfo() {
        return passengerInfo;
    }
}
