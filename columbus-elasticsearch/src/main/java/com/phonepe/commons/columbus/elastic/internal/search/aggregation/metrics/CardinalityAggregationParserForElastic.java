package com.phonepe.commons.columbus.elastic.internal.search.aggregation.metrics;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics.CardinalityAggregationParser;
import com.phonepe.commons.columbus.elastic.search.aggregation.AggregationParserForElastic;
import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

@RegistryKey(name = CardinalityAggregator.TYPE, type = CardinalityAggregator.class)
public class CardinalityAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<CardinalityAggregator, C> {

    private static final CardinalityAggregationParser PARSER = new CardinalityAggregationParser();

    @Override
    public AggregationParser<Aggregation> visit(
            final CardinalityAggregator aggregator,
            final Object props,
            final C context
    ) {
        return PARSER;
    }
}
