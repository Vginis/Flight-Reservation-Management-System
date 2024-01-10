package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import org.acme.domain.Airline;

import java.util.List;

@RequestScoped
public class AirlineRepository implements PanacheRepositoryBase<Airline, Integer> {

    public List<Airline> findAirlineByAirlineName(String name) {
            if (name == null) {
                return listAll();
            }
            return find("select a from Airline a where a.airlineName like :airlineName",
                    Parameters.with("airlineName", name + "%").map())
                    .list();
    }

    @Transactional
    public void deleteAirline(Integer id) {
        Airline airline = findById(id);
        delete(airline);
    }

    public String getMostPopularAirportByAirline(Integer id) {
        Airline airline = findById(id);
        String popularAirport;
        popularAirport = airline.mostPopularAirport();
        return popularAirport;
    }

    public Double getCompletenessByAirline(Integer id) {
        Airline airline = findById(id);
        double completeness;
        completeness = airline.completeness();
        return completeness;
    }
}
