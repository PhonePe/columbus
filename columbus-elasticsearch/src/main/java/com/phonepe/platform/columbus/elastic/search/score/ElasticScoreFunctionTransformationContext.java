package com.phonepe.platform.columbus.elastic.search.score;

import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.score.visitor.ScoreFunctionTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;

/**
 * The context used to transform {@link com.phonepe.platform.columbus.search.score.ScoreFunction ScoreFunctions} as
 * needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticScoreFunctionTransformationContext<C extends ElasticScoreFunctionTransformationContext<C>>
        extends ScoreFunctionTransformationContext<FilterFunctionBuilder, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
