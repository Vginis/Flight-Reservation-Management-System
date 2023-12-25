package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import org.acme.domain.Ticket;

@RequestScoped
public class TicketRepository implements PanacheRepositoryBase<Ticket, Integer> {
}
