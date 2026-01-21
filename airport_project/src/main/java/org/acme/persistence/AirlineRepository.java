package org.acme.persistence;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.domain.Airline;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SearchQueryCallback;

import java.util.Optional;

@RequestScoped
public class AirlineRepository extends AbstractSearchRepository<Airline> {

    public Optional<Airline> findOptionalAirlineByName(String name) {
        return find("airlineName = :name", Parameters.with("name", name))
                .firstResultOptional();
    }

    public Optional<Airline> findOptionalAirlineByU2DigitCode(String code) {
        return find("u2digitCode = :code", Parameters.with("code", code))
                .firstResultOptional();
    }

    public PageResult<Airline> searchAirlinesByParams(PageQuery<AirlineSortAndFilterBy> query){
        return searchPage(query, new SearchQueryCallback() {
            @Override
            public void defaultSort(Sort sort) {
                //left blank on purpose
            }

            @Override
            public void search(PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters) {
                appendQueryBuildAndParamsForField("airlineName"," and LOWER(airlineName) like LOWER(CONCAT('%', :airlineName, '%'))", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("u2digitCode"," and LOWER(u2digitCode) like LOWER(CONCAT('%', :u2digitCode, '%'))", query, queryBuilder, parameters);
            }
        });
    }
}
