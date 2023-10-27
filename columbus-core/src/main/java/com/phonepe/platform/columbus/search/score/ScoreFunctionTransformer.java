package com.phonepe.platform.columbus.search.score;

/**
 * An interface that transforms a given {@link ScoreFunction} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface ScoreFunctionTransformer<R> {

    R transform(ScoreFunction scoreFunction);
}
