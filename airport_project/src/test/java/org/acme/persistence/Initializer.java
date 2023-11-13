package org.acme.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.acme.domain.Airline;
import org.acme.domain.Passenger;

public class Initializer {
    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createNativeQuery("delete from AccountManagement").executeUpdate();

        tx.commit();
    }

    public void prepareData() {

        eraseData();

        Passenger p1 = new Passenger("ndiam@aueb.gr", "1234567891", "AK102545", "ndima", "yes!");

        Airline a1 = new Airline("Aegean Airlines", "A3", "aegean", "idk");

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(p1);
        em.persist(a1);

        tx.commit();

        em.close();

    }

}
