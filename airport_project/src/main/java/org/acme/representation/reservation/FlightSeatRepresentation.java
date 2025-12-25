package org.acme.representation.reservation;

import org.acme.constant.SeatReservationState;
import org.acme.domain.FlightSeatState;

public class FlightSeatRepresentation {
    private Integer rowIndex;
    private Integer columnIndex;
    private SeatReservationState seatReservationState;

    public FlightSeatRepresentation() {
    }

    public FlightSeatRepresentation(FlightSeatState flightSeatState) {
        this.rowIndex = flightSeatState.getSeatRow();
        this.columnIndex = flightSeatState.getSeatColumn();
        this.seatReservationState = flightSeatState.getState();
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public SeatReservationState getSeatReservationState() {
        return seatReservationState;
    }

    public void setSeatReservationState(SeatReservationState seatReservationState) {
        this.seatReservationState = seatReservationState;
    }
}
