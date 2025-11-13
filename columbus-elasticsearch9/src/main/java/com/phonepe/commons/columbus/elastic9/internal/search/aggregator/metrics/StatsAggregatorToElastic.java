package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.metrics;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregation;
import com.phonepe.commons.columbus.elastic9.search.aggregator.AggregatorToElastic;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.metrics.StatsAggregator;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = StatsAggregator.TYPE, type = StatsAggregator.class)
public class StatsAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<StatsAggregator, C> {

    @Override
    public Aggregation visit(final StatsAggregator aggregator, final Object props, final C context) {
        final var builder = new StatsAggregation.Builder()
                .field(aggregator.getField());
        applyIfNotNull(aggregator.getMissingValue(), FieldValue::of, builder::missing);
        return builder.build()._toAggregation();
    }
}
