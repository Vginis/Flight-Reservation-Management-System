package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.*;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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

    public Double getCompletenessByFlight(Integer id){
        Flight flight = findById(id);
        double completeness;
        completeness = flight.flightCompletness();
        return completeness;
    }

    public List<Flight> findFlightByArrivalAirport(String airportName) {
        if (airportName == null) {
            return listAll();
        }
        return find("select a from Flight a where a.arrivalAirport.airportName = :name" ,
                Parameters.with("name", airportName).map())
                .list();
    }

    public List<Flight> findFlightByDepartureTime(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime depTime = LocalDateTime.parse(datetime, formatter);
        if (depTime == null) {
            return listAll();
        }
        return find("select f from Flight f where f.departureTime = :departureTime" ,
                Parameters.with("departureTime", depTime).map())
                .list();
    }

    public List<Flight> findFlightByArrivalTime(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime arrTime = LocalDateTime.parse(datetime, formatter);
        if (arrTime == null) {
            return listAll();
        }
        return find("select f from Flight f where f.arrivalTime = :arrivalTime" ,
                Parameters.with("arrivalTime", arrTime).map())
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
