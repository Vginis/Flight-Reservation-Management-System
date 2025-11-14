package org.acme.representation.file;

public class AirlineFileRepresentation extends FileRepresentation{
    private String airlineCode;

    public AirlineFileRepresentation() {
    }

    public AirlineFileRepresentation(String filename, String filetype, byte[] content, String airlineCode) {
        super(filename, filetype, content);
        this.airlineCode = airlineCode;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }
}
