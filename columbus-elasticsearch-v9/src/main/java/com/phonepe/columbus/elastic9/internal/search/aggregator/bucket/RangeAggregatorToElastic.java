package com.phonepe.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation.Builder;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;

@RegistryKey(name = RangeAggregator.TYPE, type = RangeAggregator.class)
public class RangeAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<RangeAggregator, C> {

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final RangeAggregator aggregator,
            final Object props,
            final C context
    ) {

        final var builder = new Builder().field(aggregator.getField());

        aggregator.getRanges().stream()
                .map(MiscTransforms::toElastic)
                .forEachOrdered(builder::ranges);

        return new Aggregation.Builder().range(builder.build());
    }
}
