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

        em.createNativeQuery("delete from Tickets").executeUpdate();
        em.createNativeQuery("delete from OutFlightsReservations").executeUpdate();
        em.createNativeQuery("delete from InFlightsReservations").executeUpdate();
        em.createNativeQuery("delete from Reservations").executeUpdate();
        em.createNativeQuery("delete from Flights").executeUpdate();
        em.createNativeQuery("delete from Airports").executeUpdate();
        em.createNativeQuery("delete from AccountManagement").executeUpdate();

        tx.commit();
    }

    public void prepareData() {

        eraseData();

        Passenger p1 = new Passenger("ndiam@aueb.gr", "1234567891", "AK102545", "ndima", "yes!");

        Airline a1 = new Airline("Aegean Airlines", "A3", "aegean", "idk");
        Airline a2 = new Airline("RayanAir", "FR", "rayan", "pleba");

        Administrator ad1 = new Administrator("Mpampas","admin", "gate13");

        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        Airport ai2 = new Airport("Fumicino","Milan","Italy","FCO");

        Flight f1 = new Flight("FR8438", a2, ai1, "09:00", ai2, "12:00",  200,"Boeing-365", 100L,200);
        Flight f2 = new Flight("A3651", a1, ai2, "19:00", ai1, "21:00", 178, "Airbus-A320", 80L, 178);

        Ticket t1 = new Ticket("1A", "Bob", "Wonder", "CP152D45");

        Reservation r1 = new Reservation();
        r1.setPassenger(p1);
        r1.addOutgoingFlight(f1);
        r1.addTicket(t1);

        Reservation r2 = new Reservation();
        r2.setPassenger(p1);
        r2.addOutgoingFlight(f1);
        r2.addIngoingFlight(f2);

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(p1);

        em.persist(a1);
        em.persist(a2);

        em.persist(ad1);

        em.persist(ai1);
        em.persist(ai2);

        em.persist(f1);
        em.persist(f2);

        em.persist(t1);

        em.persist(r1);
        em.persist(r2);

        tx.commit();

        em.close();

    }

}
