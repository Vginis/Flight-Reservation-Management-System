package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airline;

import java.util.List;

@RequestScoped
public class AirlineRepository implements PanacheRepositoryBase<Airline, Integer> {

    public List<Airline> findAirlineByAirlineName(String name) {
            return find("select a from Airline a where a.airlineName like :airlineName",
                    Parameters.with("airlineName", name + "%").map())
                    .list();
    }
}
