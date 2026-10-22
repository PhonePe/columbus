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
package com.phonepe.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.elastic9.search.aggregation.AggregationParserForElastic;
import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.bucket.BucketAggregator;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class BucketAggregationParserForElastic<T extends BucketAggregator,
        C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<T, C> {

    @Override
    public AggregationParser<Aggregate> visit(final T aggregator, final Object props, final C context) {
        final var subAggregationParsers = aggregator.getSubAggregators().stream()
                .collect(Collectors.toMap(Aggregator::getName, subAggregator -> context.resolve(subAggregator, props)));
        return createParser(aggregator, subAggregationParsers, props, context);
    }

    protected abstract AggregationParser<Aggregate> createParser(
            final T aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    );
}
