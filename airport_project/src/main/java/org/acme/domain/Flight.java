package org.acme.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.acme.constant.FlightStatus;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "flight_status")
    private FlightStatus flightStatus;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @Valid
    @NotNull
    @OneToOne(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private FlightSeatLayout flightSeatLayout;

    public Flight() {
    }

    public Flight(FlightCreateRepresentation flightCreateRepresentation, Airline airline, Airport departureAirport, Airport arrivalAirport, FlightSeatLayout flightSeatLayout) {
        this.flightNumber = flightCreateRepresentation.getFlightNumber();
        this.flightUUID = UUID.randomUUID().toString();
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.departureTime = flightCreateRepresentation.getDepartureTime();
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = flightCreateRepresentation.getArrivalTime();
        this.flightSeatLayout = flightSeatLayout;
        this.flightStatus = FlightStatus.SCHEDULED;
        this.tickets = new ArrayList<>();
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

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public FlightSeatLayout getFlightSeatLayout() {
        return flightSeatLayout;
    }

    public List<Ticket> getTickets() {
        return tickets;
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

    public void updateFlight(FlightStatus newStatus){
        this.flightStatus = newStatus;
    }
}
