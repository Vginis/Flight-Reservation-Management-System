package org.acme.domain;

import jakarta.persistence.*;

@Embeddable
public class Luggage {

    private boolean luggageIncluded = false;
    @Column(name = "weight", nullable = false, length = 2)
    private int weight;

    @Column(name = "pieces", nullable = false)
    private int amount;

    public Luggage() {
        this.weight = 0;
        this.amount = 0;
    }

    public boolean isLuggageIncluded() {
        return luggageIncluded;
    }

    public void setLuggageIncluded(boolean luggageIncluded) {
        this.luggageIncluded = luggageIncluded;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (this.luggageIncluded) {
            this.weight = weight;
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        if (this.luggageIncluded) {
            this.amount = amount;
        }
    }

}
