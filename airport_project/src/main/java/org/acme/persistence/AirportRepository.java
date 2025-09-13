package org.acme.persistence;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.domain.Airport;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SearchQueryCallback;

import java.util.Optional;

@RequestScoped
public class AirportRepository extends AbstractSearchRepository<Airport> {

    public PageResult<Airport> searchUsersByParams(PageQuery<AirportSortAndFilterBy> query){
        return searchPage(query, new SearchQueryCallback() {
            @Override
            public void defaultSort(Sort sort) {
                //left blank on purpose
            }

            @Override
            public void search(PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters) {
                appendQueryBuildAndParamsForField("airportName"," and airportName like '%'||:airportName||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("city"," and city like '%'||:city||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("country"," and country like '%'||:country||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("u3digitCode"," and u3digitCode like '%'||:u3digitCode||'%'", query, queryBuilder, parameters);
            }
        });
    }

    private void appendQueryBuildAndParamsForField(String field, String sqlQuery, PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters){
        if(query.getSearchField().value().equals(field)){
            queryBuilder.append(sqlQuery);
            parameters.and(field, query.getSearchValue());
        }
    }

    public Optional<Airport> findAirportBy3DCode(String code) {
        return find("u3digitCode = :code", Parameters.with("code", code))
                .firstResultOptional();
    }

    public Optional<Airport> findAirportByName(String name) {
        return find("airportName = :name", Parameters.with("name", name))
                .firstResultOptional();
    }
}
