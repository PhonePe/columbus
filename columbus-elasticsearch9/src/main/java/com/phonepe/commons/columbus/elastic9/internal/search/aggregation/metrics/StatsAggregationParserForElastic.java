package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.commons.columbus.elastic9.search.aggregation.AggregationParserForElastic;
import com.phonepe.commons.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.metrics.StatsAggregator;

@RegistryKey(name = StatsAggregator.TYPE, type = StatsAggregator.class)
public class StatsAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<StatsAggregator, C> {

    private static final StatsAggregationParser PARSER = new StatsAggregationParser();

    @Override
    public AggregationParser<Aggregate> visit(final StatsAggregator aggregator, final Object props, final C context) {
        return PARSER;
    }
}
