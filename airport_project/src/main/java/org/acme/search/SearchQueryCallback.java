package org.acme.search;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

public interface SearchQueryCallback {
    void defaultSort(Sort sort);

    void search(PageQuery<?> query, StringBuilder queryBuilder, Parameters parameters);
}
