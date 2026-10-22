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
package com.phonepe.columbus.search.aggregator.visitor;

import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link AggregatorTransformer} that uses visitor to transform the given {@link Aggregator} to the internal
 * representation used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for transformation.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class AggregatorTransformationContext<R, P, C extends AggregatorTransformationContext<R, P, C>>
        extends VisitorContext<R, Aggregator, P, C> implements AggregatorTransformer<R, P> {

    @Override
    public R transform(final Aggregator aggregator, final P context) {
        return visit(aggregator, context);
    }

    @Override
    public R transform(final Aggregator aggregator) {
        return visit(aggregator);
    }
}
