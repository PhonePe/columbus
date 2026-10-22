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
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator.KeyedFilter;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@Slf4j
@RegistryKey(name = FiltersAggregator.TYPE, type = FiltersAggregator.class)
public class FiltersAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FiltersAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final FiltersAggregator aggregator,
            final Object props,
            final C context
    ) {
        final var queryTransformer = context.toolkit().queryTransformer();
        final var filters = aggregator.getFilters().entrySet().stream()
                .map(entry -> new KeyedFilter(entry.getKey(), queryTransformer.transform(entry.getValue(), props)))
                .toArray(KeyedFilter[]::new);

        final var builder = AggregationBuilders.filters(aggregator.getName(), filters);
        applyIfNotNull(aggregator.getOtherBucketKey(), key -> builder.otherBucket(true).otherBucketKey(key));
        return builder;
    }
}
