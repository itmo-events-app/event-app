package org.itmo.eventapp.main.model.dto.response;

import java.util.List;

public class PaginatedResult<T> {
    private long totalCount;
    private List<T> results;

    public PaginatedResult(long totalCount, List<T> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<T> getResults() {
        return results;
    }
}
