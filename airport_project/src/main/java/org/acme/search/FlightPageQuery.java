package org.acme.search;

import org.acme.constant.ValueEnum;
import org.acme.constant.search.FlightSearchParamsDTO;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;

public class FlightPageQuery extends PageQuery<FlightSortAndFilterBy>{
    private String departureDate;
    private String arrivalDate;
    private Integer departureAirportId;
    private Integer arrivalAirportId;

    public FlightPageQuery(FlightSearchParamsDTO flightSearchParamsDTO, String departureDate, String arrivalDate,
                           Integer departureAirportId, Integer arrivalAirportId) {
        super(ValueEnum.fromValue(flightSearchParamsDTO.getFlightSearchFilterParamsDTO().getSearchField(),FlightSortAndFilterBy.class),
                flightSearchParamsDTO.getFlightSearchFilterParamsDTO().getSearchValue(),
                flightSearchParamsDTO.getSize(),
                flightSearchParamsDTO.getIndex(), ValueEnum.fromValue(flightSearchParamsDTO.getSortBy(),FlightSortAndFilterBy.class),
                ValueEnum.fromValue(flightSearchParamsDTO.getSortDirection(), SortDirection.class));
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
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
