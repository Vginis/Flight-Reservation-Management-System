package org.acme.constant.search;

import org.acme.constant.ValueEnum;

public enum SortDirection implements ValueEnum {
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String value;

    SortDirection(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
