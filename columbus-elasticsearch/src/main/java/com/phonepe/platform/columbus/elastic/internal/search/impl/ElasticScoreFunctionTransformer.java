package com.phonepe.platform.columbus.elastic.internal.search.impl;

import com.phonepe.platform.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
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
