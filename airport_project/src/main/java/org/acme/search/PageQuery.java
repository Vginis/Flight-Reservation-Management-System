package org.acme.search;

import org.acme.constant.search.SortDirection;

public class PageQuery<T extends SortBy> {
    private T searchField;
    private String searchValue;
    private Integer size;
    private Integer index;
    private T sortBy;
    private SortDirection sortDirection;

    public PageQuery(T searchField, String searchValue, Integer size, Integer index, T sortBy, SortDirection sortDirection) {
        this.searchField = searchField;
        this.searchValue = searchValue;
        this.size = size;
        this.index = index;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public PageQuery(Integer size, Integer index, SortDirection sortDirection) {
        this.size = size;
        this.index = index;
        this.sortDirection = sortDirection;
    }

    public T getSearchField() {
        return searchField;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getIndex() {
        return index;
    }

    public T getSortBy() {
        return sortBy;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }
}
