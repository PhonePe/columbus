package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.metrics.CardinalityAggregationParser;
import com.phonepe.commons.columbus.elastic9.search.aggregation.AggregationParserForElastic;
import com.phonepe.commons.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;

@RegistryKey(name = CardinalityAggregator.TYPE, type = CardinalityAggregator.class)
public class CardinalityAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<CardinalityAggregator, C> {

    private static final CardinalityAggregationParser PARSER = new CardinalityAggregationParser();

    @Override
    public AggregationParser<Aggregate> visit(
            final CardinalityAggregator aggregator,
            final Object props,
            final C context
    ) {
        return PARSER;
    }
}
