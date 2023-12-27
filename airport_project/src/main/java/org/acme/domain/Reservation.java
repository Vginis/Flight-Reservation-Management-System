package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Reservations")
public class Reservation {

    @Id
    @Column(name = "reservationId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer reservationId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "passengerId", nullable = false)
    private Passenger passenger;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "OutFlightsReservations",
            joinColumns = {@JoinColumn(name = "reservationId")},
            inverseJoinColumns = {@JoinColumn(name = "flightId")})
    private List<Flight> outgoingFlights = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "InFlightsReservations",
            joinColumns = {@JoinColumn(name = "reservationId")},
            inverseJoinColumns = {@JoinColumn(name = "flightId")})
    private List<Flight> ingoingFlights = new ArrayList<>();

    private Boolean returnFlight = false;

    @Column(name = "totalPrice")
    private Long totalPrice = 0L;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.MERGE)
    private List<Ticket> ticketsList = new ArrayList<>();

    public Reservation() {
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
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

    public void setOutgoingFlights(List<Flight> outgoingFlights) {
        this.outgoingFlights = outgoingFlights;
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

    public void setIngoingFlights(List<Flight> ingoingFlights) {
        this.ingoingFlights = ingoingFlights;
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

    private void setReturnFlight() {
            this.returnFlight = true;
            this.ingoingFlights = new ArrayList<>();
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void calculateTotalPrice() {
        if (this.ticketsList.isEmpty()) return;
        for (Ticket ticket : this.ticketsList) {
            this.totalPrice += ticket.getTicketPrice();
        }
    }

    public List<Ticket> getTicketsList() {
        return new ArrayList<>(ticketsList);
    }

    public void setTicketsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
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
