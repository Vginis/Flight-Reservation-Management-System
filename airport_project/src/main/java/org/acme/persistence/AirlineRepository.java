package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airline;

import java.util.Optional;

@RequestScoped
public class AirlineRepository implements PanacheRepositoryBase<Airline, Integer> {

    public Optional<Airline> findOptionalAirlineByName(String name) {
        return find("airlineName = :name", Parameters.with("name", name))
                .firstResultOptional();
    }

    public Optional<Airline> findOptionalAirlineByU2DigitCode(String code) {
        return find("u2digitCode = :code", Parameters.with("code", code))
                .firstResultOptional();
    }
}
