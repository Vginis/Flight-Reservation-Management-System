package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.domain.Flight;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestScoped
public class FlightRepository extends AbstractSearchRepository<Flight> {

    public Double getCompletenessByFlight(Integer id) {
        Flight flight = findById(id);
        double completeness;
        //completeness = flight.flightCompletness();
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
        if(query.getSearchField().value().equals("departureAirport")){
            queryBuilder.append(" AND da.u3digitCode = :departureAirport");
            params.put("departureAirport", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("arrivalAirport")){
            queryBuilder.append(" AND aa.u3digitCode = :arrivalAirport");
            params.put("arrivalAirport", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("departureTime")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime depTime = LocalDateTime.parse(query.getSearchValue(), formatter);
            queryBuilder.append(" AND f.departureTime = :departureTime");
            params.put("departureTime", depTime);
        }
        if(query.getSearchField().value().equals("arrivalTime")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime arrTime = LocalDateTime.parse(query.getSearchValue(), formatter);
            queryBuilder.append(" AND f.arrivalTime = :arrivalTime");
            params.put("arrivalTime", arrTime);
        }
    }
}
