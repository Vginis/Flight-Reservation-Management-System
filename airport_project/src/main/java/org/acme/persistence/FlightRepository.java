package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Flight;

import java.util.List;

@RequestScoped
public class FlightRepository implements PanacheRepositoryBase<Flight, Integer> {

    public List<Flight> findByAirline(Integer airlineId) {
        if (airlineId == null) {
            return listAll();
        }
        return find("select a from Flight a where a.airline.id = :airlineId" ,
                Parameters.with("airlineId", airlineId).map())
                .list();
    }

}
