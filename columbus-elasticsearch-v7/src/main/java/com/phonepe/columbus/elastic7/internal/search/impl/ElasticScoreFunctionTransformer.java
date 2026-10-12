package com.phonepe.columbus.elastic7.internal.search.impl;

import com.phonepe.columbus.elastic7.search.score.ElasticScoreFunctionTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticScoreFunctionTransformer
        extends ElasticScoreFunctionTransformationContext<ElasticScoreFunctionTransformer> {

    @Override
    protected @NonNull ElasticScoreFunctionTransformer self() {
        return this;
    }
}
