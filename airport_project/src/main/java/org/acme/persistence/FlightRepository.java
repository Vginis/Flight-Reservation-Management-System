package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.domain.Flight;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.search.FlightPageQuery;
import org.acme.search.PageResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public static final String FLIGHT_UUID_LABEL = "flightUUID";

    public Optional<Flight> getFlightByUUID(String flightUUID) {
        return find("flightUUID = :flightUUID", Parameters.with(FLIGHT_UUID_LABEL, flightUUID)).firstResultOptional();
    }

    public PageResult<Flight> searchFlightsByParams(FlightPageQuery query){
        StringBuilder queryBuilder = new StringBuilder("SELECT f FROM Flight f ");
        queryBuilder.append("JOIN f.airline al ");
        queryBuilder.append("LEFT JOIN f.departureAirport da ");
        queryBuilder.append("LEFT JOIN f.arrivalAirport aa ");
        queryBuilder.append("WHERE 1=1");

        Map<String, Object> params = new HashMap<>();
        putParametersForFlightSearch(query, queryBuilder, params);

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

    private void putParametersForFlightSearch(FlightPageQuery query, StringBuilder queryBuilder, Map<String, Object> params){
        if(query.getSearchField()!=null && query.getSearchValue()!=null){
            if(query.getSearchField().value().equals("flightNumber")){
                queryBuilder.append(" AND f.flightNumber like '%'||:flightNumber||'%'");
                params.put("flightNumber", query.getSearchValue());
            }

            if(query.getSearchField().value().equals(FLIGHT_UUID_LABEL)){
                queryBuilder.append(" AND f.flightUUID like '%'||:flightUUID||'%'");
                params.put(FLIGHT_UUID_LABEL, query.getSearchValue());
            }

            if(query.getSearchField().value().equals("flightStatus")){
                queryBuilder.append(" AND lower(f.flightStatus) like '%'||:flightStatus||'%'");
                params.put("flightStatus", query.getSearchValue().toLowerCase());
            }
        }

        if(query.getDepartureAirportId()!=null){
            queryBuilder.append(" AND da.id = :departureAirport");
            params.put(DEPARTURE_AIRPORT_LABEL, query.getDepartureAirportId());
        }

        if(query.getArrivalAirportId()!=null){
            queryBuilder.append(" AND aa.id = :arrivalAirport");
            params.put(ARRIVAL_AIRPORT_LABEL, query.getArrivalAirportId());
        }

        if(query.getDepartureDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime depTime = LocalDateTime.parse(query.getDepartureDate(), formatter);
            queryBuilder.append(" AND f.departureTime >= :departureTime");
            params.put(DEPARTURE_TIME_LABEL, depTime);
        }

        if(query.getArrivalDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
            LocalDateTime arrTime = LocalDateTime.parse(query.getArrivalDate(), formatter);
            queryBuilder.append(" AND f.arrivalTime <= :arrivalTime");
            params.put(ARRIVAL_TIME_LABEL, arrTime.withHour(23).withMinute(59).withSecond(59));
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

            LocalDateTime depStartOfDay = depDate.atStartOfDay();
            LocalDateTime depEndOfDay = depDate.atTime(LocalTime.MAX);

            queryBuilder.append(" AND f.departureTime BETWEEN :depStartOfDay AND :depEndOfDay");
            params.put("depStartOfDay", depStartOfDay);
            params.put("depEndOfDay", depEndOfDay);
        }

        if(flightMultipleParamsSearchDTO.getArrivalDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDate arrDate = LocalDate.parse(flightMultipleParamsSearchDTO.getArrivalDate(), formatter);

            LocalDateTime arrStartOfDay = arrDate.atStartOfDay();
            LocalDateTime arrEndOfDay = arrDate.atTime(LocalTime.MAX);

            queryBuilder.append(" AND f.arrivalTime BETWEEN :arrStartOfDay AND :arrEndOfDay");
            params.put("arrStartOfDay", arrStartOfDay);
            params.put("arrEndOfDay", arrEndOfDay);
        }
    }
}
