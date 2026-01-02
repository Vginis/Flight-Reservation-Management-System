package org.acme.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.acme.constant.SeatReservationState;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id",referencedColumnName = "id")
    private Flight flight;

    @OneToMany(mappedBy = "flightSeatLayout", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<FlightSeat> reservedSeats;//todo rename the reservedSeats field to "seats" or "flightSeats"

    public FlightSeatLayout() {
    }

    public FlightSeatLayout(Aircraft aircraft) {
        this.aircraftId = aircraft.getId();
        this.reservedSeats = new ArrayList<>();
        for(int row=1; row<aircraft.getAircraftRows()+1;row++){
            for(int column=1; column<aircraft.getAircraftColumns()+1; column++){
                FlightSeat flightSeat = new FlightSeat(row, column, SeatReservationState.AVAILABLE, null, this);
                reservedSeats.add(flightSeat);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public List<FlightSeat> getReservedSeats() {
        return reservedSeats;
    }
}
