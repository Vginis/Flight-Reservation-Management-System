package org.acme.representation.reservation;

import java.util.List;
import org.acme.representation.flight.FlightRepresentation;

public class FlightSeatLayoutRepresentation {
    private Integer rows;
    private Integer columns;
    private List<FlightSeatRepresentation> flightSeatRepresentationList;
    private FlightRepresentation flightInformation;

    public FlightRepresentation getFlightInformation() {
        return flightInformation;
    }

    public void setFlightInformation(FlightRepresentation flightInformation) {
        this.flightInformation = flightInformation;
    }

    public FlightSeatLayoutRepresentation(Integer rows, Integer columns,
            List<FlightSeatRepresentation> flightSeatRepresentationList, FlightRepresentation flightInformation) {
        this.rows = rows;
        this.columns = columns;
        this.flightSeatRepresentationList = flightSeatRepresentationList;
        this.flightInformation = flightInformation;
    }

    public FlightSeatLayoutRepresentation() {
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public List<FlightSeatRepresentation> getFlightSeatRepresentationList() {
        return flightSeatRepresentationList;
    }

    public void setFlightSeatRepresentationList(List<FlightSeatRepresentation> flightSeatRepresentationList) {
        this.flightSeatRepresentationList = flightSeatRepresentationList;
    }
}
