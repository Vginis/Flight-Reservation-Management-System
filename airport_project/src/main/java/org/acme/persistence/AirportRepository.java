package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airport;
import jakarta.persistence.NoResultException;

import java.util.List;

@RequestScoped
public class AirportRepository implements PanacheRepositoryBase<Airport, Integer> {

    public Airport findAirportById(Integer id) {
        PanacheQuery<Airport> query = find("select a from Airport a where a.airportId = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }

    }
}
