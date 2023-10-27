package com.phonepe.platform.columbus.elastic.internal.search.aggregation;

import lombok.Builder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;

import java.util.List;

public class ElasticFilter extends ParsedFilter {

    @Builder
    public ElasticFilter(final String name, final long docCount, final Aggregations aggregations) {
        super();
        setName(name);
        setDocCount(docCount);
        this.aggregations = aggregations == null ? new Aggregations(List.of()) : aggregations;
    }
}
