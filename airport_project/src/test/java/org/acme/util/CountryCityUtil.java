package org.acme.util;

import org.acme.domain.City;
import org.acme.domain.Country;

public class CountryCityUtil {

    public static Country createCountry() {
        return new Country("Greece");
    }

    public static City createCity() {
        return new City("Athens", createCountry());
    }
}
