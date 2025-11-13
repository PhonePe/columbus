package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.metrics;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregation;
import com.phonepe.commons.columbus.elastic9.search.aggregator.AggregatorToElastic;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = CardinalityAggregator.TYPE, type = CardinalityAggregator.class)
public class CardinalityAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<CardinalityAggregator, C> {

    @Override
    public Aggregation visit(final CardinalityAggregator aggregator, final Object props, final C context) {
        final var builder = new CardinalityAggregation.Builder()
                .field(aggregator.getField());
        applyIfNotNull(aggregator.getMissingValue(), FieldValue::of, builder::missing);
        return builder.build()._toAggregation();
    }
}
