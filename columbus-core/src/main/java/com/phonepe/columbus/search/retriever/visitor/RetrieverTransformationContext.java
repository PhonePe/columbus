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
package com.phonepe.columbus.search.retriever.visitor;

import com.phonepe.columbus.search.retriever.Retriever;
import com.phonepe.columbus.search.retriever.RetrieverTransformer;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link RetrieverTransformer} that uses visitor to transform the given {@link Retriever} to the internal representation
 * used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for transformation.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class RetrieverTransformationContext<R, P, C extends RetrieverTransformationContext<R, P, C>>
        extends VisitorContext<R, Retriever, P, C> implements RetrieverTransformer<R, P> {

    @Override
    public R transform(final Retriever retriever, final P context) {
        return visit(retriever, context);
    }

    @Override
    public R transform(final Retriever retriever) {
        return visit(retriever);
    }
}
