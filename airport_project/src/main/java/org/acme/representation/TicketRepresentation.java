package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class TicketRepresentation {

    public Integer ticketId;
    public Integer reservation;
    public String flightNo;
    public String firstName;
    public String lastName;
    public String passportId;
    public String seatNo;
    public Boolean luggageIncluded;
    public Integer amount;
    public Integer weight;
    public Long ticketPrice;

}
