package org.acme.representation.airport;

import jakarta.validation.constraints.NotNull;

public class AirportUpdateRepresentation extends AirportCreateRepresentation{
    @NotNull private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
