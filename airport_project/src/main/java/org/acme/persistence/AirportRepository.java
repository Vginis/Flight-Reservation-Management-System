package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.domain.Airport;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SearchQueryCallback;

import java.util.List;
import java.util.Optional;

@RequestScoped
public class AirportRepository extends AbstractSearchRepository<Airport> {

    public static final String VALUE_LABEL = "value";

    public PageResult<Airport> searchAirportsByParams(PageQuery<AirportSortAndFilterBy> query){
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

    public List<Airport> smartSearchAirports(String value){
        StringBuilder queryBuilder = new StringBuilder("1 = 1");
        Parameters parameters = new Parameters();

        if(value!=null){
            queryBuilder.append(" and ( airportName like '%'||:value||'%'");
            parameters.and(VALUE_LABEL, value);
            queryBuilder.append(" or city like '%'||:value||'%'");
            parameters.and(VALUE_LABEL, value);
            queryBuilder.append(" or u3digitCode like '%'||:value||'%')");
            parameters.and(VALUE_LABEL, value);
        }
        PanacheQuery<Airport> querySearch = this.find(queryBuilder.toString(), Sort.empty(), parameters).page(0, 10);
        return querySearch.list();
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
