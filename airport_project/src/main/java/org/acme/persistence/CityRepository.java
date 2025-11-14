package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.City;

import java.util.Optional;

@ApplicationScoped
public class CityRepository implements PanacheRepository<City> {
    public Optional<City> findByName(String cityName) {
        return find("cityName = :cityName", Parameters.with("cityName", cityName))
                .firstResultOptional();
    }
}
