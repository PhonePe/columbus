package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.phonepe.commons.columbus.elastic9.search.aggregator.AggregatorToElastic;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.search.aggregator.bucket.BucketAggregator;

public abstract class BucketAggregatorToElastic<T extends BucketAggregator,
        C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<T, C> {

    @Override
    public Aggregation visit(final T aggregator, final Object props, final C context) {
        final var builder = buildBucketAggregation(aggregator, props, context);

        aggregator.getSubAggregators()
                .forEach(subAgg -> builder.aggregations(subAgg.getName(), context.transform(subAgg, props)));

        return builder.build();
    }

    protected abstract Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final T aggregator,
            final Object props,
            final C context
    );
}
