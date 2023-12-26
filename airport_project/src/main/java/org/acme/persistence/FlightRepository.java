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

    public List<Flight> findAirportByName(String airportName) {
        if (airportName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.airport.name = :name" ,
                Parameters.with("name", airportName).map())
                .list();
    }

    public List<Flight> findAirlineByName(String airlineName) {
        if (airlineName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.airline.airlineName = :airlineName" ,
                Parameters.with("airlineName", airlineName).map())
                .list();
    }

}
