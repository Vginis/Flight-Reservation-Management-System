package org.acme.domain;

import jakarta.persistence.*;
import org.acme.representation.aircraft.AircraftCreateRepresentation;
import org.acme.representation.aircraft.AircraftUpdateRepresentation;

@Entity
@Table(name = "aircrafts")
public class Aircraft {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "aircraft_name", nullable = false)
    private String aircraftName;

    @Column(name = "aircraft_capacity", nullable = false)
    private Integer aircraftCapacity;

    @Column(name = "aircraft_rows", nullable = false)
    private Integer aircraftRows;

    @Column(name = "aircraft_columns", nullable = false)
    private Integer aircraftColumns;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    public Aircraft() {
    }

    public Aircraft(AircraftCreateRepresentation aircraftCreateRepresentation, Airline airline) {
        this.aircraftName = aircraftCreateRepresentation.getAircraftName();
        this.aircraftCapacity = aircraftCreateRepresentation.getAircraftCapacity();
        this.aircraftRows = aircraftCreateRepresentation.getAircraftRows();
        this.aircraftColumns = aircraftCreateRepresentation.getAircraftColumns();
        this.airline = airline;
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

    public Integer getAircraftRows() {
        return aircraftRows;
    }

    public Integer getAircraftColumns() {
        return aircraftColumns;
    }

    public Airline getAirline() {
        return airline;
    }

    public void edit(AircraftUpdateRepresentation aircraftUpdateRepresentation){
        this.aircraftName = aircraftUpdateRepresentation.getAircraftName();
        this.aircraftCapacity = aircraftUpdateRepresentation.getAircraftCapacity();
    }

    //    public double flightCompleteness(){
//TODO Fix flight completeness
//        return 100*(1-(double)this.getAvailableSeats()/this.getAircraftCapacity());
//    }
}
