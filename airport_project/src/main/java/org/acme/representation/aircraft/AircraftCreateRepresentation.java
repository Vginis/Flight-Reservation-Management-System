package org.acme.representation.aircraft;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AircraftCreateRepresentation {
    @NotBlank
    private String aircraftName;
    @Positive
    private Integer aircraftCapacity;
    @Positive
    private Integer aircraftRows;
    @Positive
    private Integer aircraftColumns;
    @NotNull
    private Integer airlineId;

    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public Integer getAircraftCapacity() {
        return aircraftCapacity;
    }

    public void setAircraftCapacity(Integer aircraftCapacity) {
        this.aircraftCapacity = aircraftCapacity;
    }

    public Integer getAircraftRows() {
        return aircraftRows;
    }

    public void setAircraftRows(Integer aircraftRows) {
        this.aircraftRows = aircraftRows;
    }

    public Integer getAircraftColumns() {
        return aircraftColumns;
    }

    public void setAircraftColumns(Integer aircraftColumns) {
        this.aircraftColumns = aircraftColumns;
    }

    public Integer getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }
}
