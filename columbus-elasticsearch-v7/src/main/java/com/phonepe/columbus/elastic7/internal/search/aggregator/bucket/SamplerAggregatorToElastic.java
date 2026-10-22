/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
