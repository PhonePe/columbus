package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;

public class ElasticStats extends ParsedStats {

    @Builder
    public ElasticStats(
            final String name,
            final long count,
            final double min,
            final double max,
            final double sum,
            final double avg
    ) {
        super();
        setName(name);
        this.count = count;
        this.min = min;
        this.max = max;
        this.sum = sum;
        this.avg = avg;
    }
}
