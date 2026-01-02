package org.acme.representation.reservation;

import org.acme.constant.SeatReservationState;
import org.acme.domain.FlightSeat;

public class FlightSeatRepresentation {
    private Integer rowIndex;
    private Integer columnIndex;
    private SeatReservationState seatReservationState;
    private String lastUpdatedBy;

    public FlightSeatRepresentation() {
    }

    public FlightSeatRepresentation(FlightSeat flightSeat) {
        this.rowIndex = flightSeat.getSeatRow();
        this.columnIndex = flightSeat.getSeatColumn();
        this.seatReservationState = flightSeat.getState();
        this.lastUpdatedBy = flightSeat.getLastUpdatedBy();
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

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
