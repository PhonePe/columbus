package com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import com.phonepe.columbus.search.aggregation.bucket.FiltersAggregation;
import com.phonepe.columbus.search.aggregation.bucket.FiltersAggregation.Bucket;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder(toBuilder = true)
public class FiltersAggregationParser extends BucketAggregationParser {

    @Override
    public FiltersAggregation parse(final Aggregate result, final String name) {
        if (result.isFilters()) {
            return parseTerms(result.filters(), name);
        }
        throw new IllegalArgumentException("Expected FiltersAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private FiltersAggregation parseTerms(final FiltersAggregate filters, final String name) {
        final var builder = FiltersAggregation.builder()
                .name(name);
        final Collection<FiltersBucket> buckets = filters.buckets().isArray()
                ? filters.buckets().array() : filters.buckets().keyed().values();

        buckets.forEach(bucket -> builder.bucket(bucket.key(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final FiltersBucket bucket) {
        final var builder = Bucket.builder()
                .count(bucket.docCount());
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
