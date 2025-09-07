package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "luggages")
public class Luggage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "weight", nullable = false, length = 2)
    private Integer weight = 0;

    @Column(name = "pieces", nullable = false)
    private Integer amount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    public Luggage() {
    }

    public Luggage(Integer weight, Integer amount) {
        this.weight = weight;
        this.amount = amount;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
