package com.phonepe.commons.columbus.elastic.internal.search.aggregation;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.ParsedMultiBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;

import java.util.List;

public class ElasticHistogram extends ParsedHistogram {

    private final List<ElasticBucket> buckets;

    @Builder
    public ElasticHistogram(final String name, final @Singular List<ElasticBucket> buckets) {
        super();
        setName(name);
        this.buckets = buckets;
    }

    @Override
    public List<? extends Histogram.Bucket> getBuckets() {
        return buckets;
    }

    public static class ElasticBucket extends ParsedMultiBucketAggregation.ParsedBucket implements Histogram.Bucket {

        @Getter
        private final Double key;

        @Builder
        public ElasticBucket(
                final Double key,
                final long docCount,
                final Aggregations aggregations
        ) {
            super();
            this.key = key;
            setKeyAsString(key.toString());
            setDocCount(docCount);
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
