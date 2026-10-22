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
