package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Airline;
import org.acme.domain.Airport;

import java.util.List;

@RequestScoped
public class AirlineRepository implements PanacheRepositoryBase<Airline,Integer> {
    public List<Airline> search(String name) {
            if (name == null) {
                return listAll();
            }

            return find("select a from Airline a where a.name like :airlineName" ,
                    Parameters.with("airlineName", name + "%").map())
                    .list();
        }
    }

