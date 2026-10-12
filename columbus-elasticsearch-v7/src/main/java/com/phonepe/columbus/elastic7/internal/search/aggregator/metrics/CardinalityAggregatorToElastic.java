package com.phonepe.columbus.elastic7.internal.search.aggregator.metrics;

import com.phonepe.columbus.elastic7.search.aggregator.AggregatorToElastic;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.metrics.CardinalityAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

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
