package com.phonepe.columbus.elastic7.internal.search.aggregation.metrics;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.AggregationParserForElastic;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.metrics.StatsAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

@RegistryKey(name = StatsAggregator.TYPE, type = StatsAggregator.class)
public class StatsAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<StatsAggregator, C> {

    private static final StatsAggregationParser PARSER = new StatsAggregationParser();

    @Override
    public AggregationParser<Aggregation> visit(final StatsAggregator aggregator, final Object props, final C context) {
        return PARSER;
    }
}
