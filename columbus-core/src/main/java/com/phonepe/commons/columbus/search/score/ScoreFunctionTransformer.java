package com.phonepe.commons.columbus.search.score;

/**
 * An interface that transforms a given {@link ScoreFunction} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for transformation.
 */
public interface ScoreFunctionTransformer<R, C> {

    R transform(ScoreFunction scoreFunction, C context);

    R transform(ScoreFunction scoreFunction);
}
