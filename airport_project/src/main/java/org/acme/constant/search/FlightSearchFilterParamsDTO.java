package org.acme.constant.search;

public class FlightSearchFilterParamsDTO {
    private String searchField;
    private String searchValue;
    private String departureDate;
    private String arrivalDate;
    private Integer departureAirportId;
    private Integer arrivalAirportId;

    public FlightSearchFilterParamsDTO() {
    }

    public FlightSearchFilterParamsDTO(String searchField, String searchValue, String departureDate, String arrivalDate, Integer departureAirportId, Integer arrivalAirportId) {
        this.searchField = searchField;
        this.searchValue = searchValue;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getSearchField() {
        return searchField;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }


    public Integer getDepartureAirportId() {
        return departureAirportId;
    }

    public Integer getArrivalAirportId() {
        return arrivalAirportId;
    }
}
