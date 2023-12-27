package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Reservation;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Integer> {

    public List<Reservation> findReservationByPassengerId(Integer passengerId) {
        if (passengerId == null) {
            return listAll();
        }
        return find("select r from Reservation r where r.passenger.id = :id" ,
                Parameters.with("id", passengerId).map())
                .list();
    }

    @Transactional
    public void deleteReservation(Integer id){
        Reservation reservation = findById(id);
        delete(reservation);
    }

}
