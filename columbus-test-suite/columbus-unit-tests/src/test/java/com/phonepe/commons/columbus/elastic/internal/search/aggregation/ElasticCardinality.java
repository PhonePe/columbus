package com.phonepe.commons.columbus.elastic.internal.search.aggregation;

import lombok.Builder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;

public class ElasticCardinality extends ParsedCardinality {

    private final long count;

    @Builder
    public ElasticCardinality(final String name, final long count) {
        super();
        this.count = count;
        setName(name);
    }

    @Override
    public long getValue() {
        return count;
    }
}
