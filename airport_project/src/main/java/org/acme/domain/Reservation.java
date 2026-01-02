package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "reservation_uuid", nullable = false)
    private UUID reservationUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation", cascade = {CascadeType.ALL})
    private List<Ticket> ticketsList = new ArrayList<>();

    public Reservation() {
    }

    public Reservation(User user, List<Ticket> ticketsList) {
        this.reservationUUID = UUID.randomUUID();
        this.user = user;
        this.ticketsList = ticketsList;
    }

    public Integer getId() {
        return id;
    }

    public UUID getReservationUUID() {
        return reservationUUID;
    }

    public User getUser() {
        return user;
    }

    public List<Ticket> getTicketsList() {
        return new ArrayList<>(ticketsList);
    }

    public void setTicketsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
    }

}
