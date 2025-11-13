package com.phonepe.commons.columbus.elastic9.internal.search.impl;

import com.phonepe.commons.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticSortTransformer extends ElasticSortTransformationContext<ElasticSortTransformer> {

    @Override
    protected @NonNull ElasticSortTransformer self() {
        return this;
    }
}
