package org.acme.constant.search;

public class FlightSearchParamsDTO {
    private FlightSearchFilterParamsDTO flightSearchFilterParamsDTO;
    private Integer size;
    private Integer index;
    private String sortBy;
    private String sortDirection;

    public FlightSearchParamsDTO() {
    }

    public FlightSearchParamsDTO(FlightSearchFilterParamsDTO flightSearchFilterParamsDTO, Integer size, Integer index,
                                 String sortBy, String sortDirection) {
        this.flightSearchFilterParamsDTO = flightSearchFilterParamsDTO;
        this.size = size;
        this.index = index;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public FlightSearchFilterParamsDTO getFlightSearchFilterParamsDTO() {
        return flightSearchFilterParamsDTO;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getIndex() {
        return index;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }
}
