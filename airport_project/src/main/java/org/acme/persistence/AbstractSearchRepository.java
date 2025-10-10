package org.acme.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.search.SearchQueryCallback;

import java.util.Optional;

public class AbstractSearchRepository<S> implements PanacheRepositoryBase<S, Integer> {
    public PageResult<S> searchPage(PageQuery<?> query, SearchQueryCallback searchQueryCallback){
        StringBuilder queryBuilder = new StringBuilder("1 = 1");
        Parameters parameters = new Parameters();
        searchQueryCallback.search(query, queryBuilder, parameters);

        Sort sort = Sort.empty();
        if (query.getSortBy() != null && query.getSortDirection()!=null) {
            sort = toSort(query.getSortBy().value(),query.getSortDirection().value()).orElse(Sort.empty());
        }

        searchQueryCallback.defaultSort(sort);
        PanacheQuery<S> querySearch = this.find(queryBuilder.toString(), sort, parameters).page(query.getIndex(), query.getSize());
        long total = this.find(queryBuilder.toString(), parameters).count();
        return new PageResult<>(total, querySearch.list());
    }

    public void appendQueryBuildAndParamsForField(String field, String sqlQuery, PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters){
        if(query.getSearchField()!=null && query.getSearchField().value().equals(field)){
            queryBuilder.append(sqlQuery);
            parameters.and(field, query.getSearchValue());
        }
    }

    public Optional<Sort> toSort(String sortBy, String sortDirection) {
        Sort.Direction direction = (sortDirection.equals("asc")) ? Sort.Direction.Ascending : Sort.Direction.Descending;
        Sort sort = Sort.by(sortBy, direction);
        return Optional.of(sort);
    }
}
