package com.phonepe.columbus.search.score.visitor;

import com.phonepe.columbus.search.score.ScoreFunction;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link ScoreFunction} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link ScoreFunction} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface ScoreFunctionTransformationVisitor<R, T extends ScoreFunction, P,
        C extends ScoreFunctionTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, ScoreFunction, P, C> {

    @Override
    R visit(T scoreFunction, P props, C context);
}
