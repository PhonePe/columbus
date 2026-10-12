package com.phonepe.columbus.elastic9.internal.search.aggregator.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TopHitsAggregation;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.aggregator.AggregatorToElastic;
import com.phonepe.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.columbus.search.sort.Sort;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TopHitsAggregator.TYPE, type = TopHitsAggregator.class)
public class TopHitsAggregatorToElastic<T, C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<TopHitsAggregator<T>, C> {

    @Override
    public Aggregation visit(final TopHitsAggregator<T> aggregation, final Object props, final C context) {
        final var builder = new TopHitsAggregation.Builder()
                .from(aggregation.getFrom())
                .size(aggregation.getSize());
        final var sortTransformer = context.toolkit().sortTransformer();
        aggregation.getSorts().stream()
                .map((Sort sort) -> sortTransformer.transform(sort, props))
                .forEachOrdered(builder::sort);
        applyIfNotNull(aggregation.getProjection(), MiscTransforms::toElastic, builder::source);
        return builder.build()._toAggregation();
    }
}
