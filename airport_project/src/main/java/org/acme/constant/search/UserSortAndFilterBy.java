package org.acme.constant.search;

import org.acme.constant.ValueEnum;
import org.acme.search.SortBy;

public enum UserSortAndFilterBy implements ValueEnum, SortBy {
    USERNAME("username"),
    EMAIL("email"),
    PHONE_NUMBER("phoneNumber"),
    ROLE("role");

    private final String value;

    UserSortAndFilterBy(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
