package org.acme.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "flight_number", length = 20)
    private String flightNumber;

    @Column(name = "flight_uuid", length = 20)
    private UUID flightUUID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "airlineId")
    private Airline airline = new Airline();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "depAirportId")
    private Airport departureAirport = new Airport();

    @Column(name = "depTime")
    private LocalDateTime departureTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrAirportId")
    private Airport arrivalAirport = new Airport();

    @Column(name = "arrTime")
    private LocalDateTime arrivalTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aircraft_id", referencedColumnName = "id")
    private Aircraft aircraft;

    public Flight() {
    }

    public Flight(String flightNumber, UUID flightUUID, Airline airline, Airport departureAirport, LocalDateTime departureTime, Airport arrivalAirport, LocalDateTime arrivalTime) {
        this.flightNumber = flightNumber;
        this.flightUUID = flightUUID;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
    }

    public Integer getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public UUID getFlightUUID() {
        return flightUUID;
    }

    public Airline getAirline() {
        return airline;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    private void calculateAvailableSeats() {
//        Integer soldTickets = this.ticketList.size();
//        if (this.availableSeats > 0)
//            this.availableSeats = this.aircraftCapacity - soldTickets;
    }

//    public void addTicket(Ticket ticket) {
//        if (ticket == null) return;
//        if (this.availableSeats == 0)
//            throw new RuntimeException("There are no more seats available.");
//        if (ticketList.contains(ticket))
//            throw new RuntimeException("This Ticket is already on the list.");
//        ticketList.add(ticket);
//        calculateAvailableSeats();
//    }
//
//    public void removeTicket(Ticket ticket) {
//        if (ticket == null) return;
//        if (ticketList.contains(ticket)) {
//            ticketList.remove(ticket);
//            calculateAvailableSeats();
//        } else
//            throw new RuntimeException("This Ticket is not on the list.");
//    }
//
    public double flightCompleteness(){
        return 100*(1-(double)this.getAvailableSeats()/this.getAircraftCapacity());
    }

}
