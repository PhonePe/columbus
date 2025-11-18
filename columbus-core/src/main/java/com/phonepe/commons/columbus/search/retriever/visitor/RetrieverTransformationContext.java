package com.phonepe.commons.columbus.search.retriever.visitor;

import com.phonepe.commons.columbus.search.retriever.Retriever;
import com.phonepe.commons.columbus.search.retriever.RetrieverTransformer;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
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
