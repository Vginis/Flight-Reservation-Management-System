package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Flight;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestScoped
public class FlightRepository extends AbstractSearchRepository<Flight> {

    public static final String DEPARTURE_AIRPORT_LABEL = "departureAirport";
    public static final String ARRIVAL_AIRPORT_LABEL = "arrivalAirport";
    public static final String DEPARTURE_TIME_LABEL = "departureTime";
    public static final String ARRIVAL_TIME_LABEL = "arrivalTime";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public Double getCompletenessByFlight() {
        //TODO Fix
        return 0.0;
    }

    public PageResult<Flight> searchFlightsByParams(PageQuery<FlightSortAndFilterBy> query){
        StringBuilder queryBuilder = new StringBuilder("SELECT f FROM Flight f ");
        queryBuilder.append("JOIN f.airline al ");
        queryBuilder.append("LEFT JOIN f.departureAirport da ");
        queryBuilder.append("LEFT JOIN f.arrivalAirport aa ");
        queryBuilder.append("WHERE 1=1");

        Map<String, Object> params = new HashMap<>();
        if(query.getSearchField()!=null && query.getSearchValue()!=null){
            putParametersForFlightSearch(query, queryBuilder, params);
        }

        Optional<Sort> sortOptional = (query.getSortBy()!=null && query.getSortDirection()!=null) ?
                toSort(query.getSortBy().getOrderBy(), query.getSortDirection().value()) :
                Optional.of(Sort.empty());

        PanacheQuery<Flight> flightPanacheQuery = sortOptional
                .map(sort -> this.find(queryBuilder.toString(), sort, params))
                .orElseGet(() -> this.find(queryBuilder.toString(), params))
                .page(query.getIndex(), query.getSize());

        List<Flight> flightList = flightPanacheQuery.list();
        return new PageResult<>(flightList.size(), flightList);
    }

    private void putParametersForFlightSearch(PageQuery<FlightSortAndFilterBy> query, StringBuilder queryBuilder, Map<String, Object> params){
        if(query.getSearchField().value().equals("flightNumber")){
            queryBuilder.append(" AND f.flightNumber like '%'||:flightNumber||'%'");
            params.put("flightNumber", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("flightUUID")){
            queryBuilder.append(" AND f.flightUUID like '%'||:flightUUID||'%'");
            params.put("flightUUID", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("airline")){
            queryBuilder.append(" AND al.u2digitCode = :airline");
            params.put("airline", query.getSearchValue());
        }
        if(query.getSearchField().value().equals(DEPARTURE_AIRPORT_LABEL)){
            queryBuilder.append(" AND da.u3digitCode = :departureAirport");
            params.put(DEPARTURE_AIRPORT_LABEL, query.getSearchValue());
        }
        if(query.getSearchField().value().equals(ARRIVAL_AIRPORT_LABEL)){
            queryBuilder.append(" AND aa.u3digitCode = :arrivalAirport");
            params.put(ARRIVAL_AIRPORT_LABEL, query.getSearchValue());
        }
        if(query.getSearchField().value().equals(DEPARTURE_TIME_LABEL)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime depTime = LocalDateTime.parse(query.getSearchValue(), formatter);
            queryBuilder.append(" AND f.departureTime = :departureTime");
            params.put(DEPARTURE_TIME_LABEL, depTime);
        }
        if(query.getSearchField().value().equals(ARRIVAL_TIME_LABEL)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime arrTime = LocalDateTime.parse(query.getSearchValue(), formatter);
            queryBuilder.append(" AND f.arrivalTime = :arrivalTime");
            params.put(ARRIVAL_TIME_LABEL, arrTime);
        }
    }

    public PageResult<Flight> searchFlightsByMultipleParams(FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO, Integer size, Integer index){
        StringBuilder queryBuilder = new StringBuilder("SELECT f FROM Flight f ");
        queryBuilder.append("JOIN f.departureAirport da ");
        queryBuilder.append("LEFT JOIN f.arrivalAirport aa ");
        queryBuilder.append("WHERE 1=1");

        Map<String, Object> params = new HashMap<>();
        putMultipleParamsForFlightSearch(flightMultipleParamsSearchDTO, queryBuilder, params);

        Optional<Sort> sortOptional = toSort(FlightSortAndFilterBy.ARRIVAL_TIME.getOrderBy(),
                SortDirection.ASCENDING.value());
        PanacheQuery<Flight> flightPanacheQuery = sortOptional
                .map(sort -> this.find(queryBuilder.toString(), sort, params))
                .orElseGet(() -> this.find(queryBuilder.toString(), params))
                .page(index, size);

        List<Flight> flightList = flightPanacheQuery.list();
        return new PageResult<>(flightList.size(), flightList);
    }

    private void putMultipleParamsForFlightSearch(FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO,
                                                  StringBuilder queryBuilder, Map<String, Object> params){
        if(flightMultipleParamsSearchDTO.getDepartureAirport()!=null){
            queryBuilder.append(" AND da.u3digitCode = :departureAirport");
            params.put(DEPARTURE_AIRPORT_LABEL, flightMultipleParamsSearchDTO.getDepartureAirport());
        }

        if(flightMultipleParamsSearchDTO.getArrivalAirport()!=null){
            queryBuilder.append(" AND aa.u3digitCode = :arrivalAirport");
            params.put(ARRIVAL_AIRPORT_LABEL, flightMultipleParamsSearchDTO.getArrivalAirport());
        }

        if(flightMultipleParamsSearchDTO.getDepartureDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDate depDate = LocalDate.parse(flightMultipleParamsSearchDTO.getDepartureDate(), formatter);
            queryBuilder.append(" AND f.departureTime = :departureTime");
            params.put(DEPARTURE_TIME_LABEL, depDate.atStartOfDay());
        }

        if(flightMultipleParamsSearchDTO.getArrivalDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDate arrDate = LocalDate.parse(flightMultipleParamsSearchDTO.getArrivalDate(), formatter);
            queryBuilder.append(" AND f.arrivalTime = :arrivalTime");
            params.put(ARRIVAL_TIME_LABEL, arrDate.atStartOfDay());
        }
    }
}
