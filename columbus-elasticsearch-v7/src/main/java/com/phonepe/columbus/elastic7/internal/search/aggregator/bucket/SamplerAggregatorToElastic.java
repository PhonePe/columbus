package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.SamplerAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.sampler.DiversifiedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.sampler.SamplerAggregationBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;

@RegistryKey(name = SamplerAggregator.TYPE, type = SamplerAggregator.class)
public class SamplerAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<SamplerAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final SamplerAggregator aggregator,
            final Object props,
            final C context
    ) {
        return aggregator.getDiversifyField() == null ? sampler(aggregator) : diversifiedSampler(aggregator);
    }

    private SamplerAggregationBuilder sampler(final SamplerAggregator aggregator) {
        final var builder = AggregationBuilders.sampler(aggregator.getName());

        applyIfPositive(aggregator.getShardSize(), builder::shardSize);

        return builder;
    }

    private DiversifiedAggregationBuilder diversifiedSampler(final SamplerAggregator aggregator) {
        final var builder = AggregationBuilders.diversifiedSampler(aggregator.getName());

        applyIfPositive(aggregator.getShardSize(), builder::shardSize);
        applyIfNotNull(aggregator.getDiversifyField(), builder::field);
        applyIfPositive(aggregator.getMaxDuplicates(), builder::maxDocsPerValue);

        return builder;
    }
}
