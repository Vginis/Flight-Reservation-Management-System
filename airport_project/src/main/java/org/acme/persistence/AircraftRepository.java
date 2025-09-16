package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.domain.Aircraft;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class AircraftRepository extends AbstractSearchRepository<Aircraft> {

    public PageResult<Aircraft> searchAircraftByParams(PageQuery<AircraftSortAndFilterBy> query){
        StringBuilder queryBuilder = new StringBuilder("SELECT a FROM Aircraft a ");
        queryBuilder.append("JOIN a.airline al ");
        queryBuilder.append("WHERE 1=1");

        Map<String, Object> params = new HashMap<>();
        if(query.getSearchField()!=null && query.getSearchValue()!=null){
            putParametersForAircraftSearch(query, queryBuilder, params);
        }

        Optional<Sort> sortOptional = (query.getSortBy()!=null && query.getSortDirection()!=null) ?
                toSort(query.getSortBy().getOrderBy(), query.getSortDirection().value()) :
                Optional.of(Sort.empty());

        PanacheQuery<Aircraft> aircraftPanacheQuery = sortOptional
                .map(sort -> this.find(queryBuilder.toString(), sort, params))
                .orElseGet(() -> this.find(queryBuilder.toString(), params))
                .page(query.getIndex(), query.getSize());

        List<Aircraft> aircraftList = aircraftPanacheQuery.list();
        return new PageResult<>(aircraftList.size(), aircraftList);
    }

    private void putParametersForAircraftSearch(PageQuery<AircraftSortAndFilterBy> query, StringBuilder queryBuilder, Map<String, Object> params){
        if(query.getSearchField().value().equals("aircraftName")){
            queryBuilder.append(" AND a.aircraftName like '%'||:aircraftName||'%'");
            params.put("aircraftName", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("aircraftCapacity")){
            queryBuilder.append(" AND CAST(a.aircraftCapacity AS text) = :aircraftCapacity");
            params.put("aircraftCapacity", query.getSearchValue());
        }
        if(query.getSearchField().value().equals("airline")){
            queryBuilder.append(" AND al.u2digitCode = :airline");
            params.put("airline", query.getSearchValue());
        }
    }
}
