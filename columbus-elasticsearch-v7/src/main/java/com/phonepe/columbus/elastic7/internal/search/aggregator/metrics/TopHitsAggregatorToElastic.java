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
package com.phonepe.columbus.elastic7.internal.search.aggregator.metrics;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.aggregator.AggregatorToElastic;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.columbus.search.sort.Sort;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TopHitsAggregator.TYPE, type = TopHitsAggregator.class)
public class TopHitsAggregatorToElastic<T, C extends ElasticAggregatorTransformationContext<C>>
        implements AggregatorToElastic<TopHitsAggregator<T>, C> {

    @Override
    public AggregationBuilder visit(final TopHitsAggregator<T> aggregation, final Object props, final C context) {
        final var builder = AggregationBuilders.topHits(aggregation.getName())
                .from(aggregation.getFrom())
                .size(aggregation.getSize());
        final var sortTransformer = context.toolkit().sortTransformer();
        aggregation.getSorts().stream()
                .map((Sort sort) -> sortTransformer.transform(sort, props))
                .forEachOrdered(builder::sort);
        applyIfNotNull(aggregation.getProjection(), MiscTransforms::toElastic, builder::fetchSource);
        return builder;
    }
}
