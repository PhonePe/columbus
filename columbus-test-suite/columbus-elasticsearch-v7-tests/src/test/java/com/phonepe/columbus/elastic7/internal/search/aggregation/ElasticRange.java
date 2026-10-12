package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;

import java.util.List;

public class ElasticRange extends ParsedRange {

    @Builder
    public ElasticRange(final String name, final @Singular List<ParsedBucket> buckets) {
        super();
        setName(name);
        this.buckets.addAll(buckets);
    }

    public static class ElasticBucket extends ParsedBucket {

        @Builder
        public ElasticBucket(
                final String key,
                final Double from,
                final Double to,
                final long docCount,
                final Aggregations aggregations
        ) {
            super();
            setKeyAsString(key);
            if (from != null) {
                this.from = from;
            }
            if (to != null) {
                this.to = to;
            }
            setDocCount(docCount);
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
