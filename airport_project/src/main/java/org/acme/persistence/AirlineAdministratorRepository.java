package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.AirlineAdministrator;

import java.util.Optional;

@ApplicationScoped
public class AirlineAdministratorRepository implements PanacheRepository<AirlineAdministrator> {
    public Optional<AirlineAdministrator> findByUsername(String username){
        return find("username = :username", Parameters.with("username", username))
                .firstResultOptional();
    }
}
