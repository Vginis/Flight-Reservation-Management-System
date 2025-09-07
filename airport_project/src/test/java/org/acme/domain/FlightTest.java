//package org.acme.domain;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FlightTest {
//
//    Airline airline;
//    Airport airport1;
//    Airport airport2;
//    Flight flight;
//    Ticket ticket1;
//    Ticket ticket2;
//    Ticket ticket3;
//
//    @BeforeEach
//    public void setup() {
//        airline = new Airline("Aegean Airlines", "A3");//, "aegean", "JeandDig1@");
//        airport1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
//        airport2 = new Airport("Fiumicino","Rome","Italy","FCO");
//        flight = new Flight("A3651", airline, airport1, "202307192100", airport2, "202307192100", 178, "Airbus-A320", 80L);
//        Reservation reservation = new Reservation();
//        ticket1 = new Ticket(reservation, flight, "1A", "Ben", "Wales", "CD542K76");
//        ticket2 = new Ticket(reservation, flight, "2F", "Art", "Vandelay", "A754G5L");
//        ticket3 = new Ticket(reservation, flight, "2A", "Turd", "Ferguson", "R34DH88");
//    }
//
//    @Test
//    public void FlightNoLessThan2Characters() {
//        assertThrows(RuntimeException.class, () -> flight.setFlightNo("A"));
//    }
//
//    @Test
//    public void FlightNoDoesNotMatchAirline() {
//        assertThrows(RuntimeException.class, () -> flight.setFlightNo("FR"));
//    }
//
//    @Test
//    public void AirlineDoesNotMatchFlightNo() {
//        Airline airline2 = new Airline("Ryanair Ltd.", "FR");//, "ryanair", "JeandDig1@");
//        assertThrows(RuntimeException.class, () -> flight.setAirline(airline2));
//    }
//
//    @Test
//    public void AircraftCapacityIsLessThanSoldTickets() {
//        flight.addTicket(ticket1);
//        flight.addTicket(ticket2);
//        flight.addTicket(ticket3);
//        assertThrows(RuntimeException.class, () -> flight.setAircraftCapacity(2));
//    }
//
//    @Test
//    public void ThereAreNoMoreAvailableSeats() {
//        flight.addTicket(ticket1);
//        flight.addTicket(ticket2);
//        flight.setAircraftCapacity(2);
//        assertThrows(RuntimeException.class, () -> flight.addTicket(ticket3));
//    }
//
//    @Test
//    public void AddSameTicket2Times() {
//        flight.addTicket(ticket1);
//        assertThrows(RuntimeException.class, () -> flight.addTicket(ticket1));
//    }
//
//    @Test
//    public void RemoveExistingTicket() {
//        flight.addTicket(ticket1);
//        assertEquals(1, flight.getTicketList().size());
//        flight.removeTicket(ticket1);
//        assertEquals(0, flight.getTicketList().size());
//    }
//
//    @Test
//    public void RemoveNoExistTicket() {
//        flight.addTicket(ticket1);
//        assertThrows(RuntimeException.class, () -> flight.removeTicket(ticket2));
//    }
//
//    @Test
//    public void SameAirportForDepartureAndArrival() {
//        flight.setDepartureAirport(airport1);
//        assertThrows(RuntimeException.class, () -> flight.setArrivalAirport(airport1));
//        flight.setArrivalAirport(airport2);
//        assertThrows(RuntimeException.class, () -> flight.setDepartureAirport(airport2));
//    }
//
//    @Test
//    public void flightCompletnessTest(){
//        Flight flight1= new Flight("A3651", airline, airport1, "202307192100", airport2, "202307192100", 12, "Airbus-A320", 80L);
//        Reservation reservation = new Reservation();
//        ticket1 = new Ticket(reservation, flight1, "1A", "Ben", "Wales", "CD542K76");
//        ticket2 = new Ticket(reservation, flight1, "2F", "Art", "Vandelay", "A754G5L");
//        ticket3 = new Ticket(reservation, flight1, "2A", "Turd", "Ferguson", "R34DH88");
//        flight1.addTicket(ticket1);
//        flight1.addTicket(ticket2);
//        flight1.addTicket(ticket3);
//        assertEquals(25,flight1.flightCompletness());
//    }
//
//}
