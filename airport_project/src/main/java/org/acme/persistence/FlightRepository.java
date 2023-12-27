package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.*;
import jakarta.transaction.Transactional;


import java.util.List;

@RequestScoped
public class FlightRepository implements PanacheRepositoryBase<Flight, Integer> {

    public List<Flight> findFlightByAirlineId(Integer airlineId) {
        if (airlineId == null) {
            return listAll();
        }
        return find("select a from Flight a where a.airline.id = :airlineId",
                Parameters.with("airlineId", airlineId).map())
                .list();
    }

    @Transactional
    public void deleteFlight(Integer id){
        Flight flight = findById(id);
        delete(flight);
    }

    public List<Flight> findFlightByDepartureAirport(String airportName) {
        if (airportName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.departureAirport.airportName = :name" ,
                Parameters.with("name", airportName).map())
                .list();
    }

    public List<Flight> findFlightByArrivalAirport(String airportName) {
        if (airportName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.arrivalAirport.airportName = :name" ,
                Parameters.with("name", airportName).map())
                .list();
    }

/*
    public List<Flight> findAirlineByName(String airlineName) {
        if (airlineName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.airline.airlineName = :airlineName" ,
                Parameters.with("airlineName", airlineName).map())
                .list();
    }*/

}
