package com.phonepe.platform.columbus.search.score.visitor;

import com.phonepe.platform.columbus.search.score.ScoreFunction;
import com.phonepe.platform.columbus.search.score.ScoreFunctionTransformer;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link ScoreFunctionTransformer} that uses visitor to transform the given {@link ScoreFunction} to the internal
 * representation used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class ScoreFunctionTransformationContext<R, C extends ScoreFunctionTransformationContext<R, C>>
        extends VisitorContext<R, ScoreFunction, C> implements ScoreFunctionTransformer<R> {

    @Override
    public R transform(final ScoreFunction scoreFunction) {
        return visit(scoreFunction);
    }
}
