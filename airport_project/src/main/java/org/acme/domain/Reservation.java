package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "reservation_id", nullable = false)
    private UUID reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.REMOVE)
    private List<Ticket> ticketsList = new ArrayList<>();

    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void addOutgoingFlight(Flight outFlight) {
//TODO Fix
//        if (outFlight == null) return;
//        if (this.outgoingFlights.contains(outFlight)) {
//            throw new RuntimeException("Outgoing Flight is already on the list.");
//        }
//        this.outgoingFlights.add(outFlight);
    }

//   TODO Review
//    public void removeOutgoingFlight(Flight outFlight) {
//        if (outFlight == null) return;
//        if (this.outgoingFlights.contains(outFlight))
//            this.outgoingFlights.remove(outFlight);
//        else
//            throw new RuntimeException("Outgoing Flight is not on the list.");
//    }

//   TODO Review
//    public void addIngoingFlight(Flight inFlight) {
//        if (inFlight == null) return;
//        if (!this.returnFlight) setReturnFlight();
//        if (this.ingoingFlights.contains(inFlight)) {
//            throw new RuntimeException("Ingoing Flight is already on the list..");
//        }
//        this.ingoingFlights.add(inFlight);
//    }
//
//    public void removeIngoingFlight(Flight inFlight) {
//        if (inFlight == null) return;
//        if (this.ingoingFlights.contains(inFlight))
//            this.ingoingFlights.remove(inFlight);
//        else
//            throw new RuntimeException("Ingoing Flight is not on the list.");
//    }

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
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) return;
        if (this.ticketsList.contains(ticket)) {
            this.ticketsList.remove(ticket);
        } else
            throw new RuntimeException("Ticket is not on the list.");
    }

}
