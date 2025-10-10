package org.acme.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.acme.constant.SeatReservationState;
@Entity
@Table(name = "flight_seat_state")
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
}
