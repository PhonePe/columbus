package com.phonepe.commons.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.RangeAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

@RegistryKey(name = RangeAggregator.TYPE, type = RangeAggregator.class)
public class RangeAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<RangeAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final RangeAggregator aggregation,
            final Object props,
            final C context
    ) {
        final var builder = AggregationBuilders.range(aggregation.getName())
                .field(aggregation.getField());

        aggregation.getRanges().stream()
                .map(MiscTransforms::toElastic)
                .forEachOrdered(builder::addRange);

        return builder;
    }
}
