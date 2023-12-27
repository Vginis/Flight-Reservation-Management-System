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
        return find("select a from Flight a where a.departureAirport.airportName like :name" ,
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
        return find("select a from Flight a where a.arrivalAirport.airportName like :name" ,
                Parameters.with("name", airportName+ "%").map())
                .list();
    }

    public List<Flight> findFlightByDepartureTime(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime depTime = LocalDateTime.parse(datetime, formatter);
        return find("select f from Flight f where f.departureTime = :departureTime" ,
                Parameters.with("departureTime", depTime).map())
                .list();
    }

    public List<Flight> findFlightByArrivalTime(String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime arrTime = LocalDateTime.parse(datetime, formatter);
        return find("select f from Flight f where f.arrivalTime = :arrivalTime" ,
                Parameters.with("arrivalTime", arrTime).map())
                .list();
    }

    public List<Flight> findFlightByParameters (String depAirport, String arrAirport, String dpTime, String arTime, Integer passCount) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime depTime = LocalDateTime.parse(dpTime, formatter1);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime arrTime = LocalDateTime.parse(arTime, formatter2);

        return find("select f from Flight f where (f.arrivalTime = :arrivalTime) AND (f.departureTime = :departureTime) AND (f.departureAirport.airportName = :dname) AND (f.arrivalAirport.airportName = :aname) AND ((f.availableSeats - :passCount) > 0)",
                Parameters.with("arrivalTime", arrTime)
                        .and("departureTime", depTime)
                        .and("dname", depAirport)
                        .and("aname", arrAirport)
                        .and("passCount", passCount)
                        .map()).list();

    }

}
