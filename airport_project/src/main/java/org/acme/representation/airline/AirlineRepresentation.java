package org.acme.representation.airline;

import org.acme.representation.file.FileRepresentation;

public class AirlineRepresentation{
    private Integer id;
    private String airlineName;
    private String u2digitCode;
    private FileRepresentation fileRepresentation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getU2digitCode() {
        return u2digitCode;
    }

    public void setU2digitCode(String u2digitCode) {
        this.u2digitCode = u2digitCode;
    }

    public FileRepresentation getFileRepresentation() {
        return fileRepresentation;
    }

    public void setFileRepresentation(FileRepresentation fileRepresentation) {
        this.fileRepresentation = fileRepresentation;
    }
}
