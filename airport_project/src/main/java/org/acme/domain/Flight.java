package org.acme.domain;

import jakarta.persistence.*;
import org.acme.util.SystemDate;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name="Flight")
public class Flight {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="UFNo",nullable = false,length = 20)
    private String UFNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name="departureDateTime",nullable = false)
    private LocalDate d1;

    /*
    @Column(name="arrivalDateTime",nullable = false)
    private Date d2;
*/
    @Column(name="aircraftCapacity",nullable = false)
    private Integer capacity;

    @Column(name="aircraftType",nullable = false,length = 20)
    private String aircraftType;

    @Column(name="TicketPrice",nullable = false)
    private Integer ticketPrice;

    @Column(name="availableSeats", nullable = false)
    private Integer seats;


    public Flight() {
    }

    public Flight(String ufNo, Integer capacity,String aircraftType,Integer ticketPrice,Integer seats){
        this.UFNo = ufNo;
        this.d1 = SystemDate.now();
        this.capacity = capacity;
        this.aircraftType = aircraftType;
        this.ticketPrice = ticketPrice;
        this.seats = seats;
    }


    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }
}
