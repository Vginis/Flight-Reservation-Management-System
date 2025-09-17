package org.acme.constant.search;

import org.acme.constant.ValueEnum;
import org.acme.search.SortBy;

public enum AirlineSortAndFilterBy implements ValueEnum, SortBy {
    AIRLINE_NAME("airlineName"),
    U2DIGIT_CODE("u2digitCode");

    private final String value;

    AirlineSortAndFilterBy(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
