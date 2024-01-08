package com.phonepe.commons.columbus.elastic.internal.search.aggregation.metrics;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics.StatsAggregationParser;
import com.phonepe.commons.columbus.elastic.search.aggregation.AggregationParserForElastic;
import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.metrics.StatsAggregator;
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
