package com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.metrics.StatsAggregation;

public class StatsAggregationParser implements ElasticAggregationParser {

    @Override
    public StatsAggregation parse(final Aggregate result, final String name) {
        if (result.isStats()) {
            return parseStats(result.stats(), name);
        }
        throw new IllegalArgumentException("Expected StatsAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private StatsAggregation parseStats(final StatsAggregate stats, final String name) {
        return StatsAggregation.builder()
                .name(name)
                .count(stats.count())
                .minimum(stats.min())
                .maximum(stats.max())
                .sum(stats.sum())
                .average(stats.avg())
                .build();
    }
}
