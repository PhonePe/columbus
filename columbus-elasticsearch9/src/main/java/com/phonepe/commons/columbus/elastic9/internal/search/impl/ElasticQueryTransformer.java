package com.phonepe.commons.columbus.elastic9.internal.search.impl;

import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticQueryTransformer extends ElasticQueryTransformationContext<ElasticQueryTransformer> {

    @Override
    protected @NonNull ElasticQueryTransformer self() {
        return this;
    }
}
