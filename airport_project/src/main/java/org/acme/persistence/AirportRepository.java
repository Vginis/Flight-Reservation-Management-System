package org.acme.persistence;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;

import org.acme.domain.Airport;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

@RequestScoped
public class AirportRepository implements PanacheRepositoryBase<Airport, Integer> {

    public List<Airport> search(String name) {
        if (name == null) {
            return listAll();
        }

        return find("select airport from Airport airport where airport.name like :airportName" ,
                Parameters.with("airportName", name + "%").map())
                .list();
    }
}