package org.acme.representation.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class TicketCreateRepresentation {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String passport;
    @NotNull
    private Integer row;
    @NotNull
    private Integer column;
    @NotNull
    private List<@Positive Integer> luggageWeights;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public List<Integer> getLuggageWeights() {
        return luggageWeights;
    }

    public void setLuggageWeights(List<Integer> luggageWeights) {
        this.luggageWeights = luggageWeights;
    }
}
