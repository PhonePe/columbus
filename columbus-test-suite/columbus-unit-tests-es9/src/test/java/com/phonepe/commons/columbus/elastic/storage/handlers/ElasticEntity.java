package com.phonepe.commons.columbus.elastic.storage.handlers;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
class ElasticEntity {

    private final String id;

    private final String name;

    private final int count;

    // to create exception scenarios with type errors
    private final Object attribute;
}
