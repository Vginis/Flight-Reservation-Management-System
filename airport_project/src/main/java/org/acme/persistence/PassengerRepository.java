package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Passenger;

import java.util.List;

@RequestScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger,Integer> {

    //I have to change the name here
    public List<Passenger> searchByName(String name) {
        if (name == null) {
            return listAll();
        }

        return find("select a from Passenger a where a.name like :passengerName" ,
                Parameters.with("passengerName", name + "%").map())
                .list();
    }

    /*
    @Transactional
    public void delete(Integer id){
        Passenger passenger = findById(id);
        passenger.clearReservations();
        delete(passenger);
    }

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

