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

    @Transactional
    public void deletePassenger(Integer id) {
        Passenger passenger = findById(id);
        passenger.clearReservations();
        delete(passenger);
    }

}

