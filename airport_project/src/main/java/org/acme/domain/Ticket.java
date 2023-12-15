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
    private Luggage luggage;

    @Column(name = "ticketPrice", nullable = false)
    private long ticketPrice;

    @Column(name = "seatNo", nullable = false)
    private String seatNo;

    @Embedded
    private PassengerInfo passengerInfo;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flightId", nullable = false)
    private Flight flight;

    public Ticket() {
        passengerInfo = new PassengerInfo();
    }

    public Ticket(Reservation reservation, Flight flight, String seatNo, String firstName, String lastName, String passportId) {
        this.reservation = reservation;
        this.flight = flight;
        this.seatNo = seatNo;
        this.passengerInfo = new PassengerInfo(firstName, lastName, passportId);
        this.ticketPrice = flight.getTicketPrice();
        luggage = new Luggage();
    }

    public Reservation getReservation() {
        return reservation;
    }

    public boolean isLuggageIncluded() {
        return luggage.isLuggageIncluded();
    }

    public void setLuggageIncluded(boolean luggageIncluded) {
        this.luggage.setLuggageIncluded(luggageIncluded);
        calculateTicketPrice();
    }

    public int getWeight() {
        return luggage.getWeight();
    }

    public void setWeight(int weight) {
        this.luggage.setWeight(weight);
    }

    public int getAmount() {
        return luggage.getAmount();
    }

    public void setAmount(int amount) {
        this.luggage.setAmount(amount);
    }

    public long getTicketPrice() {
        return ticketPrice;
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

}
