package com.phonepe.commons.columbus.elastic.internal.search.impl;

import com.phonepe.commons.columbus.elastic.search.page.ElasticPageResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticPageResolver extends ElasticPageResolutionContext<ElasticPageResolver> {

    @Override
    protected @NonNull ElasticPageResolver self() {
        return this;
    }
}
