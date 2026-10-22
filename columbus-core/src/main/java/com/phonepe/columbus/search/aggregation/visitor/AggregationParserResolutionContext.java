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
package com.phonepe.columbus.search.aggregation.visitor;

import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregation.AggregationParserResolver;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link AggregationParserResolver} that uses visitor to resolve the {@link AggregationParser} for a given
 * {@link Aggregator}.
 *
 * @param <R> the type used by storage for aggregation results.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class AggregationParserResolutionContext<R, P, C extends AggregationParserResolutionContext<R, P, C>>
        extends VisitorContext<AggregationParser<R>, Aggregator, P, C> implements AggregationParserResolver<R, P> {

    @Override
    public AggregationParser<R> resolve(final Aggregator aggregator, final P context) {
        return visit(aggregator, context);
    }

    @Override
    public AggregationParser<R> resolve(final Aggregator aggregator) {
        return visit(aggregator);
    }
}
