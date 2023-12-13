package com.phonepe.platform.columbus.elastic.internal.search.aggregator.metrics;

import com.phonepe.platform.columbus.elastic.search.aggregator.AggregatorToElastic;
import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregator.metrics.StatsAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = StatsAggregator.TYPE, type = StatsAggregator.class)
public class StatsAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<StatsAggregator, C> {

    @Override
    public AggregationBuilder visit(final StatsAggregator aggregator, final Object props, final C context) {
        final var builder = AggregationBuilders.stats(aggregator.getName())
                .field(aggregator.getField());
        applyIfNotNull(aggregator.getMissingValue(), builder::missing);
        return builder;
    }
}
