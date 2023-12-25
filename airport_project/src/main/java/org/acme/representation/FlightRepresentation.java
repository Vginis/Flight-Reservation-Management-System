package org.acme.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDateTime;
import java.util.List;

@RegisterForReflection
public class FlightRepresentation {

    public Integer id;
    public String flightNo;
    public String airlineName;
    public String departureAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    public LocalDateTime departureTime;
    public String arrivalAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    public LocalDateTime arrivalTime;
    public Integer aircraftCapacity;
    public String aircraftType;
    public Long ticketPrice;
    public Integer availableSeats;
    public List<Integer> ticketList;

}
