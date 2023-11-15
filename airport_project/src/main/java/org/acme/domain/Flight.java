package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name="Flights")
public class Flight {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name="FlightNo", nullable = false, length = 20)
    private String flightNo;

    @ManyToOne
    @JoinColumn (name = "airlineId", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "depAirportId", nullable = false)
    private Airport departureAirport;

    @Column(name = "depTime", nullable = false)
    private String departureTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrAirportId", nullable = false)
    private Airport arrivalAirport;

    @Column(name = "arrTime", nullable = false)
    private String arrivalTime;

    @Column(name="aircraftCapacity", nullable = false)
    private Integer capacity;

    @Column(name="aircraftType", nullable = false, length = 20)
    private String aircraftType;

    @Column(name="TicketPrice", nullable = false)
    private long ticketPrice;

    @Column(name="availableSeats", nullable = false)
    private Integer availableSeats;


    public Flight() {
    }

    public Flight(String flightNo, Airline airline, Airport departureAirport, String departureTime, Airport arrivalAirport, String arrivalTime, Integer capacity, String aircraftType, long ticketPrice, Integer availableSeats) {
        this.flightNo = flightNo;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
        this.aircraftType = aircraftType;
        this.ticketPrice = ticketPrice;
        this.availableSeats = availableSeats;
    }

    public Integer getId() {
        return id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public long getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Long ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }
}
