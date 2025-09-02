package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "aircraftName", nullable = false)
    private String aircraftName;

    @Column(name = "aircraftCapacity", nullable = false)
    private Integer aircraftCapacity;

    @Column(name = "aircraftType", nullable = false)
    private String aircraftType;

    @OneToOne(mappedBy = "aircraft")
    private Flight flight;

    public Aircraft() {
    }

    public Aircraft(String aircraftName, Integer aircraftCapacity, String aircraftType, Flight flight) {
        this.aircraftName = aircraftName;
        this.aircraftCapacity = aircraftCapacity;
        this.aircraftType = aircraftType;
        this.flight = flight;
    }

    public Integer getId() {
        return id;
    }

    public String getAircraftName() {
        return aircraftName;
    }

    public Integer getAircraftCapacity() {
        return aircraftCapacity;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public Flight getFlight() {
        return flight;
    }

    public double flightCompleteness(){
        return 100*(1-(double)this.getAvailableSeats()/this.getAircraftCapacity());
    }
}
