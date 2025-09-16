package org.acme.domain;

import jakarta.persistence.*;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "flight_number", length = 20)
    private String flightNumber;

    @Column(name = "flight_uuid", columnDefinition = "CHAR(36)", length = 40, unique = true)
    private String flightUUID;

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

    private Integer aircraftId;

    public Flight() {
    }

    public Flight(FlightCreateRepresentation flightCreateRepresentation, Airline airline, Airport departureAirport, Airport arrivalAirport) {
        this.flightNumber = flightCreateRepresentation.getFlightNumber();
        this.flightUUID = UUID.randomUUID().toString();
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.departureTime = flightCreateRepresentation.getDepartureTime();
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = flightCreateRepresentation.getArrivalTime();
        this.aircraftId = flightCreateRepresentation.getAircraftId();
    }

    public Integer getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFlightUUID() {
        return flightUUID;
    }

    public Integer getAircraftId() {
        return aircraftId;
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
//    public double flightCompleteness(){
//        return 100*(1-(double)this.getAvailableSeats()/this.getAircraftCapacity());
//    }

    public void editDates(FlightDateUpdateRepresentation flightDateUpdateRepresentation){
        this.departureTime = flightDateUpdateRepresentation.getDepartureTime();
        this.arrivalTime = flightDateUpdateRepresentation.getArrivalTime();
    }
}
