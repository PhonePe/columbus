package com.phonepe.platform.columbus.elastic.internal.dao.impl;

import com.phonepe.platform.columbus.elastic.dao.id.ElasticIdResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticIdResolver extends ElasticIdResolutionContext<ElasticIdResolver> {

    @Override
    protected @NonNull ElasticIdResolver self() {
        return this;
    }
}
