package com.phonepe.commons.columbus.elastic.internal.search.aggregator.metrics;

import com.phonepe.commons.columbus.elastic.search.aggregator.AggregatorToElastic;
import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = CardinalityAggregator.TYPE, type = CardinalityAggregator.class)
public class CardinalityAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<CardinalityAggregator, C> {

    @Override
    public AggregationBuilder visit(final CardinalityAggregator aggregator, final Object props, final C context) {
        final var builder = AggregationBuilders.cardinality(aggregator.getName())
                .field(aggregator.getField());
        applyIfNotNull(aggregator.getMissingValue(), builder::missing);
        return builder;
    }
}
