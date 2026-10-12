package com.phonepe.columbus.elastic9.internal.dao.impl;

import com.phonepe.columbus.elastic9.dao.index.ElasticIndexResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticIndexResolver extends ElasticIndexResolutionContext<ElasticIndexResolver> {

    @Override
    protected @NonNull ElasticIndexResolver self() {
        return this;
    }
}
