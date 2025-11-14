package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Country;

import java.util.Optional;

@ApplicationScoped
public class CountryRepository implements PanacheRepository<Country> {
    public Optional<Country> findByName(String countryName) {
        return find("countryName = :countryName", Parameters.with("countryName", countryName))
                .firstResultOptional();
    }
}
