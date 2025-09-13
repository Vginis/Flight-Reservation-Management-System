package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Passenger;

import java.util.Optional;

@RequestScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger, Integer> {

    public Optional<Passenger> findPassengerByPassport(String passport){
        return find("passport = :passport", Parameters.with("passport", passport))
                .firstResultOptional();
    }
}

