package com.phonepe.platform.columbus.search.score.visitor;

import com.phonepe.platform.columbus.search.score.ScoreFunction;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link ScoreFunction} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link ScoreFunction} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface ScoreFunctionTransformationVisitor<R, T extends ScoreFunction,
        C extends ScoreFunctionTransformationContext<R, C>>
        extends EntityVisitor<R, T, ScoreFunction, C> {

    @Override
    R visit(T scoreFunction, C context);
}
