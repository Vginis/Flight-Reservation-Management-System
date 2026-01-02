package org.acme.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.acme.representation.reservation.TicketCreateRepresentation;

@Embeddable
public class PassengerInfo {

    @Column(name = "firstName", nullable = false, length = 30)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 30)
    private String lastName;

    @Column(name = "passport", nullable = false, length = 10)
    private  String passport;

    public PassengerInfo() {
    }

    public PassengerInfo(TicketCreateRepresentation ticketCreateRepresentation) {
        this.firstName = ticketCreateRepresentation.getFirstName();
        this.lastName = ticketCreateRepresentation.getLastName();
        this.passport = ticketCreateRepresentation.getPassport();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassport() {
        return passport;
    }
}
