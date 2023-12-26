package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;

@RequestScoped
public class AirportRepository implements PanacheRepositoryBase<Airport, Integer> {

    @Transactional
    public void deleteAirport(Integer id){
        Airport airport = findById(id);
        delete(airport);
    }

    public Airport findAirportById(Integer id) {
        PanacheQuery<Airport> query = find("select a from Airport a where a.airportId = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch(NoResultException ex) {
            return null;
        }

    }

}
