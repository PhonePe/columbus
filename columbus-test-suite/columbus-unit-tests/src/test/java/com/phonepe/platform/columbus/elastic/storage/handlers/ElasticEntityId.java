package com.phonepe.platform.columbus.elastic.storage.handlers;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
class ElasticEntityId {

    private final String id;
}
