package com.phonepe.commons.columbus.elastic9.internal.search.impl;

import com.phonepe.commons.columbus.elastic9.search.retriever.ElasticRetrieverTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticRetrieverTransformer extends ElasticRetrieverTransformationContext<ElasticRetrieverTransformer> {

    @Override
    protected @NonNull ElasticRetrieverTransformer self() {
        return this;
    }
}

