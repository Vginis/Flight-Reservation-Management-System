package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "flight_seat_layouts")
public class FlightSeatLayout {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "aircraft_id", nullable = false)
    private Integer aircraftId;

    @Column(name = "seat_rows", nullable = false)
    private Integer seatRows;

    @Column(name = "seat_columns", nullable = false)
    private Integer seatColumns;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id",referencedColumnName = "id")
    private Flight flight;

    @OneToMany(mappedBy = "flightSeatLayout", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<FlightSeatState> reservedSeats;

    public FlightSeatLayout() {
    }

    public FlightSeatLayout(Aircraft aircraft) {
        this.aircraftId = aircraft.getId();
        this.seatRows = aircraft.getAircraftRows();
        this.seatColumns = aircraft.getAircraftColumns();
        this.reservedSeats = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public Integer getSeatRows() {
        return seatRows;
    }

    public Integer getSeatColumns() {
        return seatColumns;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<FlightSeatState> getReservedSeats() {
        return reservedSeats;
    }
}
