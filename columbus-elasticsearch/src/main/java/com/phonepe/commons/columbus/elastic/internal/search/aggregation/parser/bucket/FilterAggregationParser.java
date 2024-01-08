package com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket;

import com.phonepe.commons.columbus.search.aggregation.bucket.FilterAggregation;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;

@SuperBuilder(toBuilder = true)
public class FilterAggregationParser extends BucketAggregationParser {

    @Override
    public FilterAggregation parse(final Aggregation result) {
        if (result instanceof ParsedFilter filter) {
            return parseFilter(filter);
        }
        throw new IllegalArgumentException("Expected ParsedFilter, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private FilterAggregation parseFilter(final ParsedFilter filter) {
        final var builder = FilterAggregation.builder()
                .name(filter.getName())
                .count(filter.getDocCount());
        parseSubAggregations(filter.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
