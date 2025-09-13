package org.acme.mapper;

import org.acme.search.PageResult;

import java.util.Collection;
import java.util.List;

public interface OneWayMapper<D,S> {
    D map(S e);
    Collection<D> map(Collection<S> s);
    List<D> map(List<S> s);
    Collection<D> map(S[] s);

    default PageResult<D> map(PageResult<S> e) {
        if ( e == null ) {
            return null;
        }

        return new PageResult<>(e.getTotal(), e.getResults().stream().map(this::map).toList());
    }
}
