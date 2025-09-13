package org.acme.constant.search;

import org.acme.constant.ValueEnum;
import org.acme.search.SortBy;

public enum AirportSortAndFilterBy implements ValueEnum, SortBy {
    AIRPORT_NAME("airportName"),
    CITY("city"),
    COUNTRY("country"),
    U_3_DIGIT_CODE("u3digitCode");

    private final String value;

    AirportSortAndFilterBy(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
