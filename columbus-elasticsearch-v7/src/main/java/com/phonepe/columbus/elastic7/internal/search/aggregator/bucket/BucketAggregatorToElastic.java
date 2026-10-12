package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.search.aggregator.AggregatorToElastic;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.search.aggregator.bucket.BucketAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public abstract class BucketAggregatorToElastic<T extends BucketAggregator,
        C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<T, C> {

    @Override
    public AggregationBuilder visit(final T aggregator, final Object props, final C context) {
        final var builder = buildBucketAggregation(aggregator, props, context);
        aggregator.getSubAggregators().stream()
                .map(subAggregator -> context.transform(subAggregator, props))
                .forEachOrdered(builder::subAggregation);
        return builder;
    }

    protected abstract AggregationBuilder buildBucketAggregation(
            final T aggregator,
            final Object props,
            final C context
    );
}
