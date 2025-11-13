package com.phonepe.commons.columbus.elastic9.internal.dao.impl;

import com.phonepe.commons.columbus.elastic9.dao.id.ElasticIdResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticIdResolver extends ElasticIdResolutionContext<ElasticIdResolver> {

    @Override
    protected @NonNull ElasticIdResolver self() {
        return this;
    }
}
