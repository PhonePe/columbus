package com.phonepe.commons.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.sampler.DiversifiedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.sampler.SamplerAggregationBuilder;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.commons.columbus.common.Predicates.applyIfPositive;

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
