package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Reservations")
public class Reservation {

    @Id
    @Column(name="bookingId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passengerId", nullable = false)
    private Passenger passenger;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "OutFlightsReservations",
            joinColumns = {@JoinColumn(name = "flightId")},
            inverseJoinColumns = {@JoinColumn(name = "reservationId")})
    private List<Flight> outgoingFlights = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "InFlightsReservations",
            joinColumns = {@JoinColumn(name = "flightId")},
            inverseJoinColumns = {@JoinColumn(name = "reservationId")})
    private List<Flight> ingoingFlights;

    private Boolean returnFlight = false;

    @Column(name = "totalPrice", nullable = false)
    private long totalPrice;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<Ticket> ticketsList = new ArrayList<>();

    public Reservation() {
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public List<Flight> getOutgoingFlights() {
        return new ArrayList<>(outgoingFlights);
    }

    public void addOutgoingFlight(Flight outFlight) {
        if (outFlight == null) return;
        if (outgoingFlights.contains(outFlight)) {
            throw new RuntimeException("Outgoing Flight already exists.");
        }
        outgoingFlights.add(outFlight);
    }

    public void removeOutgoingFlight(Flight outFlight) {
        outgoingFlights.remove(outFlight);
    }

    public List<Flight> getIngoingFlights() {
        return new ArrayList<>(ingoingFlights);
    }

    public void addIngoingFlight(Flight inFlight) {
        if (inFlight == null) return;
        if (!returnFlight) setReturnFlight(true);
        if (ingoingFlights.contains(inFlight)) {
            throw new RuntimeException("Ingoing Flight already exists.");
        }
        ingoingFlights.add(inFlight);
    }

    public void removeIngoingFlight(Flight inFlight) {
        ingoingFlights.remove(inFlight);
    }

    public Boolean getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Boolean returnFlight) {
        if (returnFlight) {
            this.returnFlight = true;
            this.ingoingFlights = new ArrayList<>();
        }
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Ticket> getTicketsList() {
        return ticketsList;
    }

    public void setTicketsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
    }

}
