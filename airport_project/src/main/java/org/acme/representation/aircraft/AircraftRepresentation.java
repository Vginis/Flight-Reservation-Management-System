package org.acme.representation.aircraft;

public class AircraftRepresentation {
    private Integer id;
    private String aircraftName;
    private Integer aircraftCapacity;
    private String airline2DigitCode;

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

    public String getAirline2DigitCode() {
        return airline2DigitCode;
    }

    public void setAirline2DigitCode(String airline2DigitCode) {
        this.airline2DigitCode = airline2DigitCode;
    }
}
