package com.phonepe.platform.columbus.dao.index;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
public class IndexProperties {

    private final Integer numberOfShards;

    private final Integer numberOfReplicas;

    private final Map<String, Object> schema;

    private final Map<String, Object> settings;
}
