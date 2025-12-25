package org.acme.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.acme.constant.SeatReservationState;
@Entity
@Table(name = "flight_seat_state")//TODO Rename it to flight_seat
public class FlightSeatState {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "seat_row", nullable = false)
    private Integer seatRow;

    @Column(name = "seat_column", nullable = false)
    private Integer seatColumn;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state", nullable = false,length = 20)
    private SeatReservationState state;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "flight_seat_layout_id", referencedColumnName = "id")
    private FlightSeatLayout flightSeatLayout;

    public FlightSeatState() {
    }

    public FlightSeatState(Integer seatRow, Integer seatColumn, SeatReservationState state, FlightSeatLayout flightSeatLayout) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.state = state;
        this.flightSeatLayout = flightSeatLayout;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public Integer getSeatColumn() {
        return seatColumn;
    }

    public SeatReservationState getState() {
        return state;
    }

    public FlightSeatLayout getFlightSeatLayout() {
        return flightSeatLayout;
    }

    public void updateSeatState(SeatReservationState seatReservationState) {
        this.state = seatReservationState;
    }
}
