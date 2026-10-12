package com.phonepe.columbus.elastic9.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.score.visitor.ScoreFunctionTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to transform {@link com.phonepe.columbus.search.score.ScoreFunction ScoreFunctions} as
 * needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticScoreFunctionTransformationContext<C extends ElasticScoreFunctionTransformationContext<C>>
        extends ScoreFunctionTransformationContext<FunctionScore, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
