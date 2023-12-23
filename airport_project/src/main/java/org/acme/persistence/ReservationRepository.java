package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Reservation;
import jakarta.persistence.NoResultException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import java.util.List;

@RequestScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Integer> {

    public Reservation findReservationById(Integer id) {
        PanacheQuery<Reservation> query = find("select r from Reservation r where r.reservationId = :id", Parameters.with("id", id).map());
        try {
            return query.singleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }
}
