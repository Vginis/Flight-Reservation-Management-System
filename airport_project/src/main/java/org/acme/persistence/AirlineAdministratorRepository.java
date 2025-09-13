package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.AirlineAdministrator;
@ApplicationScoped
public class AirlineAdministratorRepository implements PanacheRepository<AirlineAdministrator> {
}
