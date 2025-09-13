package org.acme.search;

import java.util.List;

public class PageResult<T> {
    private final long total;
    private final List<T> results;

    public PageResult(long total, List<T> results) {
        this.total = total;
        this.results = results;
    }

    public long getTotal() {
        return total;
    }

    public List<T> getResults() {
        return results;
    }
}
