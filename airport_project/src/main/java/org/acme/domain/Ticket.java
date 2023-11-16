package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @Column(name="ticketId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer ticketId;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservationId", nullable = false)
    private Reservation reservation;
*/
    @Embedded
    private Luggage luggage;

    @Column(name = "ticketPrice", nullable = false)
    private long ticketPrice;

    @Column(name = "seatNo", nullable = false)
    private String seatNo;

    @Embedded
    private PassengerInfo passengerInfo;

    public Ticket() {
        passengerInfo = new PassengerInfo();
    }

    public Ticket(String seatNo, String firstName, String lastName, String passportId) {
        this.seatNo = seatNo;
        this.passengerInfo = new PassengerInfo(firstName, lastName, passportId);
        luggage = new Luggage();
    }

    /*public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
*/
    public boolean isLuggageIncluded() {
        return luggage.isLuggageIncluded();
    }

    public void setLuggageIncluded(boolean luggageIncluded) {
        this.luggage.setLuggageIncluded(luggageIncluded);
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

    public void setTicketPrice(long ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    //TODO the calculateTicketPrice()
    public void calculateTicketPrice() {
        ticketPrice = 0;
        if (isLuggageIncluded()) ticketPrice += 30;
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

}
