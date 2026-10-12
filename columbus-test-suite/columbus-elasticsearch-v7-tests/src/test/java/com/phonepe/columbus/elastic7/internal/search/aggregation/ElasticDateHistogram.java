package com.phonepe.columbus.elastic7.internal.search.aggregation;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.ParsedMultiBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class ElasticDateHistogram extends ParsedDateHistogram {

    @Getter
    private final List<ElasticBucket> buckets;

    @Builder
    public ElasticDateHistogram(final String name, final @Singular List<ElasticBucket> buckets) {
        super();
        setName(name);
        this.buckets = buckets;
    }

    public static class ElasticBucket extends ParsedMultiBucketAggregation.ParsedBucket implements Histogram.Bucket {

        @Getter
        private final ZonedDateTime key;

        @Builder
        public ElasticBucket(
                final Long key,
                final long docCount,
                final Aggregations aggregations
        ) {
            super();
            this.key = Instant.ofEpochMilli(key).atZone(ZoneOffset.UTC);
            setKeyAsString(key.toString());
            setDocCount(docCount);
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
