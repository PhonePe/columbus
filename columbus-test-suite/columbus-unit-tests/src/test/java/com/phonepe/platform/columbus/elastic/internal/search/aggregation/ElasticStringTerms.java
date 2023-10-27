package com.phonepe.platform.columbus.elastic.internal.search.aggregation;

import lombok.Builder;
import lombok.Singular;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;

import java.util.List;

public class ElasticStringTerms extends ParsedStringTerms {

    @Builder
    public ElasticStringTerms(
            final String name,
            final @Singular List<ParsedBucket> buckets,
            final long countError,
            final long otherDocCounts
    ) {
        super();
        setName(name);
        this.buckets.addAll(buckets);
        this.docCountErrorUpperBound = countError;
        this.sumOtherDocCount = otherDocCounts;
    }

    public static class ElasticBucket extends ParsedBucket {

        @Builder
        public ElasticBucket(
                final String key,
                final long docCount,
                final long docCountError,
                final Aggregations aggregations
        ) {
            super();
            setKeyAsString(key);
            setDocCount(docCount);
            this.docCountError = docCountError;
            setAggregations(aggregations == null ? new Aggregations(List.of()) : aggregations);
        }
    }
}
