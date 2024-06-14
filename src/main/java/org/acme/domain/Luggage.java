package org.acme.domain;

import jakarta.persistence.*;

@Embeddable
public class Luggage {

    private Boolean luggageIncluded = false;

    @Column(name = "weight", nullable = false, length = 2)
    private Integer weight = 0;

    @Column(name = "pieces", nullable = false)
    private Integer amount = 0;

    public Luggage() {
    }

    public Boolean isLuggageIncluded() {
        return luggageIncluded;
    }

    public void setLuggageIncluded(boolean luggageIncluded) {
        if (luggageIncluded) this.luggageIncluded = true;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        if (this.luggageIncluded) this.weight = weight;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        if (this.luggageIncluded) this.amount = amount;
    }

}
