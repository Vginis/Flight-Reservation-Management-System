package org.acme.constant;

public enum FlightStatus implements ValueEnum{
    SCHEDULED("SCHEDULED"),
    IN_FLIGHT("IN_FLIGHT"),
    ARRIVED("ARRIVED"),
    CANCELLED("CANCELLED"),
    DELAYED("DELAYED");

    private final String value;

    FlightStatus(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }
}
