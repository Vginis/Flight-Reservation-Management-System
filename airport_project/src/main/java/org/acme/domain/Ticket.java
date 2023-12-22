package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @Column(name="ticketId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer ticketId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "reservationId", nullable = false)
    private Reservation reservation;

    @Embedded
    private Luggage luggage = new Luggage();

    @Column(name = "ticketPrice", nullable = false)
    private Long ticketPrice = 0L;

    @Column(name = "seatNo", nullable = false)
    private String seatNo;

    @Embedded
    private PassengerInfo passengerInfo = new PassengerInfo();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flightId", nullable = false)
    private Flight flight;

    public Ticket() {
    }

    public Ticket(Reservation reservation, Flight flight, String seatNo, String firstName, String lastName, String passportId) {
        this.reservation = reservation;
        this.flight = flight;
        this.seatNo = seatNo;
        this.passengerInfo = new PassengerInfo(firstName, lastName, passportId);
        this.ticketPrice = flight.getTicketPrice();
        luggage = new Luggage();
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Boolean isLuggageIncluded() {
        return luggage.isLuggageIncluded();
    }

    public void setLuggageIncluded(boolean luggageIncluded) {
        this.luggage.setLuggageIncluded(luggageIncluded);
        calculateTicketPrice();
    }

    public Integer getWeight() {
        return luggage.getWeight();
    }

    public void setWeight(int weight) {
        this.luggage.setWeight(weight);
    }

    public Integer getAmount() {
        return luggage.getAmount();
    }

    public void setAmount(int amount) {
        this.luggage.setAmount(amount);
    }

    public Long getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Long ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    private void calculateTicketPrice() {
        if (this.flight == null)
            throw new RuntimeException("Flight is null.");
        this.ticketPrice = flight.getTicketPrice();
        if (isLuggageIncluded()) this.ticketPrice += 30;

    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
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
