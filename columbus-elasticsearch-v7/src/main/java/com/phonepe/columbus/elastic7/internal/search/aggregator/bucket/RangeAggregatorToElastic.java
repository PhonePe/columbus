package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

@RegistryKey(name = RangeAggregator.TYPE, type = RangeAggregator.class)
public class RangeAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<RangeAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final RangeAggregator aggregator,
            final Object props,
            final C context
    ) {
        final var builder = AggregationBuilders.range(aggregator.getName())
                .field(aggregator.getField());

        aggregator.getRanges().stream()
                .map(MiscTransforms::toElastic)
                .forEachOrdered(builder::addRange);

        return builder;
    }
}
