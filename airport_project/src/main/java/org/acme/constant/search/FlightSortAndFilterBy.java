package org.acme.constant.search;

import org.acme.constant.ValueEnum;
import org.acme.search.SortBy;

public enum FlightSortAndFilterBy implements ValueEnum, SortBy {
    FLIGHT_NUMBER("flightNumber"),
    FLIGHT_UUID("flightUUID"),
    AIRLINE("airline","al.u2digitCode"),
    DEPARTURE_AIRPORT("departureAirport", "da.u3digitCode"),
    DEPARTURE_TIME("departureTime"),
    ARRIVAL_AIRPORT("arrivalAirport", "aa.u3digitCode"),
    ARRIVAL_TIME("arrivalTime");

    private final String value;
    private final String orderBy;

    FlightSortAndFilterBy(String value) {
        this.value = value;
        this.orderBy = value;
    }

    FlightSortAndFilterBy(String value,String orderBy) {
        this.value = value;
        this.orderBy = orderBy;
    }

    @Override
    public String value() {
        return value;
    }

    public String getOrderBy(){
        return orderBy;
    }
}