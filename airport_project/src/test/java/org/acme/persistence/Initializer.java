package org.acme.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.acme.domain.*;


public class Initializer {
    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.createNativeQuery("delete from Flights").executeUpdate();
        em.createNativeQuery("delete from Airports").executeUpdate();
        em.createNativeQuery("delete from AccountManagement").executeUpdate();

        tx.commit();
    }

    public void prepareData() {

        eraseData();

        Passenger p1 = new Passenger("ndiam@aueb.gr", "1234567891", "AK102545", "ndima", "yes!");
        Airline a1 = new Airline("Aegean Airlines", "A3", "aegean", "idk");
        Administrator ad1 = new Administrator("Mpampas","admin", "gate13");
        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        Airport ai2 = new Airport("Fumicino","Milan","Italy","FCO");

        Flight f1 = new Flight("FR8438", a1, ai1, "09:00", ai2, "12:00",  200,"Boeing-365", (long) 100,200);

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(p1);
        em.persist(a1);
        em.persist(ad1);

        em.persist(ai1);
        em.persist(ai2);

        em.persist(f1);
        tx.commit();

        em.close();

    }

}
