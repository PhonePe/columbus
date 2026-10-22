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
package com.phonepe.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RegistryKey(name = FilterAggregator.TYPE, type = FilterAggregator.class)
public class FilterAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FilterAggregator, C> {

    private static final Query DEFAULT_FILTER = MatchAllQuery.builder().build();

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final FilterAggregator aggregator,
            final Object props,
            final C context
    ) {
        if (aggregator.getFilter() == null) {
            log.warn("FilterAggregator(name={}) has no filter", aggregator.getName());
        }
        final var filter = Objects.requireNonNullElse(aggregator.getFilter(), DEFAULT_FILTER);

        final QueryVariant query = context.toolkit().queryTransformer().transform(filter, props);

        return new Aggregation.Builder().filter(query._toQuery());
    }
}
