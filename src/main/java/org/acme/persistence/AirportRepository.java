package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;

import java.util.List;

@RequestScoped
public class AirportRepository implements PanacheRepositoryBase<Airport, Integer> {

    @Transactional
    public void deleteAirport(Integer id) {
        Airport airport = findById(id);
        delete(airport);
    }

    public List<Airport> findAirportBy3DCode(String code) {
        if (code == null) {
            return listAll();
        }
        return find("select a from Airport a where a.u3digitCode like :code",
                Parameters.with("code", code + "%").map())
                .list();
    }

}
