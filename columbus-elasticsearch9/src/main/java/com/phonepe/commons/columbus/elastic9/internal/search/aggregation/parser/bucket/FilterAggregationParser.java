package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FilterAggregate;
import com.phonepe.commons.columbus.search.aggregation.bucket.FilterAggregation;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class FilterAggregationParser extends BucketAggregationParser {

    @Override
    public FilterAggregation parse(final Aggregate result, final String name) {
        if (result.isFilter()) {
            return parseFilter(result.filter(), name);
        }
        throw new IllegalArgumentException("Expected FilterAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private FilterAggregation parseFilter(final FilterAggregate filter, final String name) {
        final var builder = FilterAggregation.builder()
                .name(name)
                .count(filter.docCount());
        parseSubAggregations(filter.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
