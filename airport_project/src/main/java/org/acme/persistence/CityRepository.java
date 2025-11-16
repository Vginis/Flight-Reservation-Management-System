package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.City;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CityRepository implements PanacheRepository<City> {

    public Optional<City> findByName(String cityName) {
        return find("cityName = :cityName", Parameters.with("cityName", cityName))
                .firstResultOptional();
    }

    public List<City> smartSearchCities(String value, String countryName) {
        StringBuilder query = new StringBuilder("FROM City c WHERE 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (value != null && !value.isBlank()) {
            query.append(" AND LOWER(c.cityName) LIKE LOWER(CONCAT('%', :value, '%'))");
            params.put("value", value);
        }

        if (countryName != null && !countryName.isBlank()) {
            query.append(" AND LOWER(c.country.countryName) LIKE LOWER(CONCAT('%', :countryName, '%'))");
            params.put("countryName", countryName);
        }

        PanacheQuery<City> q = find(query.toString(), params).page(0, 10);
        return q.list();
    }

}
