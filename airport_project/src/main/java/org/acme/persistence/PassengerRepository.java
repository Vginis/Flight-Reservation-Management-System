package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Passenger;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger, Integer> {

    public List<Passenger> findPassengerByEmail(String email) {
        if (email == null) {
            return listAll();
        }
        return find("select a from Passenger a where a.email like :email",
                Parameters.with("email", email + "%").map())
                .list();
    }

    // TODO
    @Transactional
    public void deletePassenger(Integer id){
        Passenger passenger = findById(id);
        passenger.clearReservations();
        delete(passenger);
    }

   /*
    @Transactional
    public Passenger fetchWithReservations(Integer passengerId){
        Query query = getEntityManager().createQuery(
                " select a from Passenger a" +
                        " left join fetch a.reservations r where a.id = :id");
        query.setParameter("id",passengerId);
        Passenger passenger = (Passenger) query.getSingleResult();
        return passenger;
    }*/
}

