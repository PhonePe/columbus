package com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket;

import com.phonepe.commons.columbus.search.aggregation.bucket.FiltersAggregation;
import com.phonepe.commons.columbus.search.aggregation.bucket.FiltersAggregation.Bucket;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilters;

@SuperBuilder(toBuilder = true)
public class FiltersAggregationParser extends BucketAggregationParser {

    @Override
    public FiltersAggregation parse(final Aggregation result) {
        if (result instanceof ParsedFilters filters) {
            return parseTerms(filters);
        }
        throw new IllegalArgumentException("Expected ParsedFilters, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private FiltersAggregation parseTerms(final ParsedFilters filters) {
        final var builder = FiltersAggregation.builder()
                .name(filters.getName());
        filters.getBuckets().forEach(bucket -> builder.bucket(bucket.getKeyAsString(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final Filters.Bucket bucket) {
        final var builder = Bucket.builder()
                .count(bucket.getDocCount());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
