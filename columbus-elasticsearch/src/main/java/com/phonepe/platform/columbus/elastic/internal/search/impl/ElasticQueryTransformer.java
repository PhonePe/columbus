package com.phonepe.platform.columbus.elastic.internal.search.impl;

import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticQueryTransformer extends ElasticQueryTransformationContext<ElasticQueryTransformer> {

    @Override
    protected @NonNull ElasticQueryTransformer self() {
        return this;
    }
}
