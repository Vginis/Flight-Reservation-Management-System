package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Reservation;

import java.util.List;

@RequestScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Integer> {

    public List<Reservation> search(Integer passengerId) {
        if (passengerId == null) {
            return listAll();
        }
        return find("select r from Reservation r where r.passenger = :id" ,
                Parameters.with("id", passengerId).map())
                .list();
    }

}
