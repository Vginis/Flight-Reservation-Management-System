package org.acme.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.acme.domain.Passenger;
import org.acme.domain.Airline;
import org.acme.domain.Airport;
import org.acme.domain.Administrator;
import org.acme.domain.AccountManagement;

import java.util.List;

public class Initializer {
    private EntityManager em;

    public Initializer() {
        em = JPAUtil.getCurrentEntityManager();
    }

    private void eraseData() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //em.createNativeQuery("delete from AccountManagement").executeUpdate();
        em.createNativeQuery("delete from Passenger").executeUpdate();
        em.createNativeQuery("delete from Airline").executeUpdate();
        em.createNativeQuery("delete from Administrator").executeUpdate();
        tx.commit();
    }

    private void viewPassengers() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query query = em.createNativeQuery("select * from Passenger passenger");
        List<Passenger> results = query.getResultList();
        tx.commit();
    }

    private void viewAirlines() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query query = em.createNativeQuery("select * from Airline airline");
        List<Airline> results = query.getResultList();
        tx.commit();
    }

    private void viewAdministrator() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Query query = em.createNativeQuery("select * from Administrator administrator");
        List<Administrator> results = query.getResultList();
        tx.commit();
    }

    public void prepareData() {

        eraseData();

        Passenger p1 = new Passenger("ndiam@aueb.gr", "1234567891", "AK102545", "ndima", "yes!");
        Airline a1 = new Airline("Aegean Airlines", "A3", "aegean", "idk");
        Administrator d1 = new Administrator("sd", "sd", "sd");

       //viewPassengers();
        viewAirlines();
       //viewAdministrator();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(p1);
        em.persist(a1);
        em.persist(d1);
        tx.commit();

        em.close();

    }

}
