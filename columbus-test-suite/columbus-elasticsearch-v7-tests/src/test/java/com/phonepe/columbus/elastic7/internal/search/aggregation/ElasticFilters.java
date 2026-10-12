package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilters;

import java.util.List;

public class ElasticFilters extends ParsedFilters {

    @Builder
    public ElasticFilters(final String name, final @Singular List<ParsedBucket> buckets) {
        super();
        setName(name);
        this.buckets.addAll(buckets);
    }

    public static class ElasticBucket extends ParsedBucket {

        private final String key;

        @Builder
        public ElasticBucket(
                final String key,
                final long docCount,
                final Aggregations aggregations
        ) {
            super();
            this.key = key;
            setDocCount(docCount);
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }

        @Override
        public String getKeyAsString() {
            return key;
        }
    }
}
