package com.phonepe.commons.columbus.elastic.internal.search.aggregator.metrics;

import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.search.aggregator.AggregatorToElastic;
import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.commons.columbus.search.sort.Sort;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TopHitsAggregator.TYPE, type = TopHitsAggregator.class)
public class TopHitsAggregatorToElastic<T, C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<TopHitsAggregator<T>, C> {

    @Override
    public AggregationBuilder visit(final TopHitsAggregator<T> aggregation, final Object props, final C context) {
        final var builder = AggregationBuilders.topHits(aggregation.getName())
                .from(aggregation.getFrom())
                .size(aggregation.getSize());
        final var sortTransformer = context.toolkit().sortTransformer();
        aggregation.getSorts().stream()
                .map((Sort sort) -> sortTransformer.transform(sort, props))
                .forEachOrdered(builder::sort);
        applyIfNotNull(aggregation.getProjection(), MiscTransforms::toElastic, builder::fetchSource);
        return builder;
    }
}
