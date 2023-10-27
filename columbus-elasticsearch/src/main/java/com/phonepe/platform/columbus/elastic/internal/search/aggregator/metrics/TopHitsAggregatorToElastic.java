package com.phonepe.platform.columbus.elastic.internal.search.aggregator.metrics;

import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.elastic.search.aggregator.AggregatorToElastic;
import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregator.metrics.TopHitsAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TopHitsAggregator.TYPE, type = TopHitsAggregator.class)
public class TopHitsAggregatorToElastic<T, C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<TopHitsAggregator<T>, C> {

    @Override
    public AggregationBuilder visit(final TopHitsAggregator<T> aggregation, final C context) {
        final var builder = AggregationBuilders.topHits(aggregation.getName())
                .from(aggregation.getFrom())
                .size(aggregation.getSize());
        aggregation.getSorts().stream()
                .map(context.toolkit().sortTransformer()::transform)
                .forEachOrdered(builder::sort);
        applyIfNotNull(aggregation.getProjection(), MiscTransforms::toElastic, builder::fetchSource);
        return builder;
    }
}
