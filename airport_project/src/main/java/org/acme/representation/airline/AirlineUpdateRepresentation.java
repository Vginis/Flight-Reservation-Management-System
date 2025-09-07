package org.acme.representation;

import jakarta.validation.constraints.NotNull;

public class AirlineUpdateRepresentation extends AirlineCreateRepresentation{
    @NotNull
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
