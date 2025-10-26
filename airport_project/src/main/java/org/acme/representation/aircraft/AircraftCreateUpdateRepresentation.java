package org.acme.representation.aircraft;

import jakarta.validation.constraints.*;

public class AircraftCreateUpdateRepresentation {
    @NotBlank
    private String aircraftName;
    @Positive @NotNull
    private Integer aircraftCapacity;
    @NotNull @Min(1) @Max(60)
    private Integer aircraftRows;
    @NotNull @Min(1) @Max(10)
    private Integer aircraftColumns;

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
}
