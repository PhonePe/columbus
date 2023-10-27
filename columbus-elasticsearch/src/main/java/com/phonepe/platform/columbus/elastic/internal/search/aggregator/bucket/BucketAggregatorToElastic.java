package com.phonepe.platform.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.platform.columbus.elastic.search.aggregator.AggregatorToElastic;
import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.platform.columbus.search.aggregator.bucket.BucketAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public abstract class BucketAggregatorToElastic<T extends BucketAggregator,
        C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<T, C> {

    @Override
    public AggregationBuilder visit(final T aggregator, final C context) {
        final var builder = buildBucketAggregation(aggregator, context);
        aggregator.getSubAggregations().stream()
                .map(context::transform)
                .forEachOrdered(builder::subAggregation);
        return builder;
    }

    protected abstract AggregationBuilder buildBucketAggregation(final T aggregator, final C context);
}
