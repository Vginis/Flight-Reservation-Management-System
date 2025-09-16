package org.acme.persistence;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.domain.User;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SearchQueryCallback;

import java.util.Optional;

@ApplicationScoped
public class UserRepository extends AbstractSearchRepository<User> {

    public PageResult<User> searchUsersByParams(PageQuery<?> query){
        return searchPage(query, new SearchQueryCallback() {
            @Override
            public void defaultSort(Sort sort) {
                //left blank on purpose
            }

            @Override
            public void search(PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters) {
                appendQueryBuildAndParamsForField("username"," and username like '%'||:username||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("email"," and email like '%'||:email||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("phoneNumber"," and phoneNumber like '%'||:phoneNumber||'%'", query, queryBuilder, parameters);
                appendQueryBuildAndParamsForField("role"," and CAST(role) AS text like '%'||:role||'%'", query, queryBuilder, parameters);
            }
        });
    }

    public Optional<User> findUserByUsername(String username) {
        return find("username = :username", Parameters.with("username", username))
                .firstResultOptional();
    }
}
