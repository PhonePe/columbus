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
package com.phonepe.columbus.elastic9.search.aggregation;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregation.visitor.AggregationParserResolutionVisitor;
import com.phonepe.columbus.search.aggregator.Aggregator;

/**
 * An interface to resolve an {@link AggregationParser
 * AggregationParser} for an {@link Aggregator}'s result.
 *
 * @param <T> the type of the {@link Aggregator} whose parser is to be resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface AggregationParserForElastic<T extends Aggregator,
        C extends ElasticAggregationParserResolutionContext<C>>
        extends AggregationParserResolutionVisitor<Aggregate, T, Object, C> {
}
