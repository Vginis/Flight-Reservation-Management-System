package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Passenger;

import java.util.List;

@RequestScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger,Integer> {

    public List<Passenger> searchByName(String name) {
        if (name == null) {
            return listAll();
        }

        return find("select a from Passenger a where a.name like :passengerName" ,
                Parameters.with("passengerName", name + "%").map())
                .list();
    }
}

