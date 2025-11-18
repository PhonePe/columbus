package com.phonepe.commons.columbus.search.retriever.visitor;

import com.phonepe.commons.columbus.search.retriever.Retriever;
import com.phonepe.commons.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Retriever} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Retriever} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface RetrieverTransformationVisitor<R, T extends Retriever, P, C extends RetrieverTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, Retriever, P, C> {

    @Override
    R visit(T retriever, P props, C context);
}
