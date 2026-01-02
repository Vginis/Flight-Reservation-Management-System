package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Reservation;

@RequestScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Integer> {
}
