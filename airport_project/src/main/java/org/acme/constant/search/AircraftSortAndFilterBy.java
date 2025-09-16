package org.acme.constant.search;

import org.acme.constant.ValueEnum;
import org.acme.search.SortBy;

public enum AircraftSortAndFilterBy implements ValueEnum, SortBy {
    AIRCRAFT_NAME("aircraftName"),
    AIRCRAFT_CAPACITY("aircraftCapacity"),
    AIRLINE("airline","al.u2digitCode");

    private final String value;
    private final String orderBy;

    AircraftSortAndFilterBy(String value) {
        this.value = value;
        this.orderBy = value;
    }

    AircraftSortAndFilterBy(String value,String orderBy) {
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
