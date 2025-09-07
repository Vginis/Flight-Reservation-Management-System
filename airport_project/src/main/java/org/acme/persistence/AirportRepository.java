package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;

import java.util.Optional;

@RequestScoped
public class AirportRepository implements PanacheRepositoryBase<Airport, Integer> {

    @Transactional
    public void deleteAirport(Integer id) {
        Airport airport = findById(id);
        delete(airport);
    }

    public Optional<Airport> findAirportBy3DCode(String code) {
        return find("u3digitCode = :code", Parameters.with("code", code))
                .firstResultOptional();
    }

    public Optional<Airport> findAirportByName(String name) {
        return find("airportName = :name", Parameters.with("name", name))
                .firstResultOptional();
    }
}
