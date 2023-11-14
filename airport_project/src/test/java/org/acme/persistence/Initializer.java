package org.acme.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.acme.domain.Airline;
import org.acme.domain.Airport;
import org.acme.domain.Flight;
import org.acme.domain.Passenger;


public class Initializer {
    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createNativeQuery("delete from Airport").executeUpdate();
        em.createQuery("delete from Flight");
        em.createNativeQuery("delete from AccountManagement").executeUpdate();
        tx.commit();
    }

    public void prepareData() {

        eraseData();

        Passenger p1 = new Passenger("ndiam@aueb.gr", "1234567891", "AK102545", "ndima", "yes!");
        Airline a1 = new Airline("Aegean Airlines", "A3", "aegean", "idk");

        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        Airport ai2 = new Airport("Fumicino","Milan","Italy","FUM");

        Flight f1 = new Flight("FR87438",  200,"Boeing-365",100,200);
        f1.setAirline(a1);

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(p1);
        em.persist(a1);

        em.persist(ai1);
        em.persist(ai2);

        em.persist(f1);
        tx.commit();

        em.close();

    }

}
