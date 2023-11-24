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
    private List<Flight> outgoingFlights;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "InFlightsReservations",
            joinColumns = {@JoinColumn(name = "flightId")},
            inverseJoinColumns = {@JoinColumn(name = "reservationId")})
    private List<Flight> ingoingFlights;

    private Boolean returnFlight;

    @Column(name = "reservation", nullable = false)
    private long totalPrice;

    @OneToMany(mappedBy = "ticketId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Ticket> ticketsList;

    public Reservation() {
        outgoingFlights = new ArrayList<>();
        returnFlight = false;
        totalPrice = 0;
        ticketsList = new ArrayList<>();
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        if (passenger == null) return;
        this.passenger = passenger;
    }

    public List<Flight> getOutgoingFlights() {
        return new ArrayList<>(outgoingFlights);
    }

    public void addOutgoingFlight(Flight outFlight) {
        if (outFlight == null) return;
        if (this.outgoingFlights.contains(outFlight)) {
            throw new RuntimeException("Outgoing Flight is already on the list.");
        }
        this.outgoingFlights.add(outFlight);
    }

    public void removeOutgoingFlight(Flight outFlight) {
        if (outFlight == null) return;
        if (this.outgoingFlights.contains(outFlight))
            this.outgoingFlights.remove(outFlight);
        else
            throw new RuntimeException("Outgoing Flight is not on the list.");
    }

    public List<Flight> getIngoingFlights() {
        return new ArrayList<>(ingoingFlights);
    }

    public void addIngoingFlight(Flight inFlight) {
        if (inFlight == null) return;
        if (!this.returnFlight) setReturnFlight();
        if (this.ingoingFlights.contains(inFlight)) {
            throw new RuntimeException("Ingoing Flight is already on the list..");
        }
        this.ingoingFlights.add(inFlight);
    }

    public void removeIngoingFlight(Flight inFlight) {
        if (inFlight == null) return;
        if (this.ingoingFlights.contains(inFlight))
            this.ingoingFlights.remove(inFlight);
        else
            throw new RuntimeException("Ingoing Flight is not on the list.");
    }

    public Boolean getReturnFlight() {
        return returnFlight;
    }

    private void setReturnFlight() {
            this.returnFlight = true;
            this.ingoingFlights = new ArrayList<>();
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    private void calculateTotalPrice() {
        if (this.ticketsList.size() == 0) return;
        for (int i=0; i< this.ticketsList.size(); i++) {
            this.totalPrice += this.ticketsList.get(i).getTicketPrice();
        }
    }

    public List<Ticket> getTicketsList() {
        return new ArrayList<>(ticketsList);
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) return;
        if (this.ticketsList.contains(ticket)) {
            throw new RuntimeException("Ticket is already on the list.");
        }
        this.ticketsList.add(ticket);
        calculateTotalPrice();
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) return;
        if (this.ticketsList.contains(ticket)) {
            this.ticketsList.remove(ticket);
            calculateTotalPrice();
        } else
            throw new RuntimeException("Ticket is not on the list.");
    }

}
