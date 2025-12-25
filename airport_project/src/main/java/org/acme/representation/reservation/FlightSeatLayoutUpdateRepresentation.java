package org.acme.representation.reservation;

import org.acme.constant.SeatReservationState;

public class FlightSeatLayoutUpdateRepresentation {
    private String flightUUID;
    private Integer rowIndex;
    private Integer columnIndex;
    private SeatReservationState seatReservationState;

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
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
