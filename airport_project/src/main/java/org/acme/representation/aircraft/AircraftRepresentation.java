package org.acme.representation.aircraft;

public class AircraftRepresentation {
    private Integer id;
    private String aircraftName;
    private Integer aircraftCapacity;
    private Integer aircraftRows;
    private Integer aircraftColumns;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
