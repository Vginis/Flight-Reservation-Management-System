package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.Country;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CountryRepository implements PanacheRepository<Country> {

    public Optional<Country> findByName(String countryName) {
        return find("countryName = :countryName", Parameters.with("countryName", countryName))
                .firstResultOptional();
    }

    public List<Country> smartSearchCountries(String value){
        StringBuilder queryBuilder = new StringBuilder("1 = 1");
        Parameters parameters = new Parameters();

        if(value!=null){
            queryBuilder.append(" and countryName like '%'||:value||'%'");
            parameters.and("value", value);
        }
        PanacheQuery<Country> querySearch =
                this.find(queryBuilder.toString(), Sort.empty(), parameters).page(0, 10);
        return querySearch.list();
    }
}
