package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Reservation;

import java.util.List;

@RequestScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Integer> {

    public List<Reservation> search(Integer reservationId) {
        if (reservationId == null) {
            return listAll();
        }
        return find("select r from Reservation r where r.reservationId = :reservationId" ,
                Parameters.with("reservationId", reservationId).map())
                .list();
    }

}
