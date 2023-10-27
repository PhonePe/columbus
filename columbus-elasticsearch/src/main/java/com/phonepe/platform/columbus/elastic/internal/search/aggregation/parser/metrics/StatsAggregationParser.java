package com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.metrics;

import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.platform.columbus.search.aggregation.metrics.StatsAggregation;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;

public class StatsAggregationParser implements ElasticAggregationParser {

    @Override
    public StatsAggregation parse(final Aggregation result) {
        if (result instanceof ParsedStats stats) {
            return parseStats(stats);
        }
        throw new IllegalArgumentException("Expected ParsedStats, got '%s'".formatted(result));
    }

    private StatsAggregation parseStats(final ParsedStats stats) {
        return StatsAggregation.builder()
                .name(stats.getName())
                .count(stats.getCount())
                .minimum(stats.getMin())
                .maximum(stats.getMax())
                .sum(stats.getSum())
                .average(stats.getAvg())
                .build();
    }
}
