package com.phonepe.commons.columbus.elastic.internal.dao.impl;

import com.phonepe.commons.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticNodeIdResolver extends ElasticNodeIdResolutionContext<ElasticNodeIdResolver> {

    @Override
    protected @NonNull ElasticNodeIdResolver self() {
        return this;
    }
}
