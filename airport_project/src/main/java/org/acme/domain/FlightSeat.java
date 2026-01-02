package org.acme.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.acme.constant.SeatReservationState;

@Entity
@Table(name = "flight_seat")
public class FlightSeat {
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

    @Column(name = "last_updated_by", length = 100)
    private String lastUpdatedBy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "flight_seat_layout_id", referencedColumnName = "id")
    private FlightSeatLayout flightSeatLayout;

    public FlightSeat() {
    }

    public FlightSeat(Integer seatRow, Integer seatColumn, SeatReservationState state, String lastUpdatedBy, FlightSeatLayout flightSeatLayout) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.state = state;
        this.lastUpdatedBy = lastUpdatedBy;
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public FlightSeatLayout getFlightSeatLayout() {
        return flightSeatLayout;
    }

    public void updateSeatState(SeatReservationState seatReservationState, String updatedByUsername) {
        this.state = seatReservationState;
        this.lastUpdatedBy = updatedByUsername;
    }
}
