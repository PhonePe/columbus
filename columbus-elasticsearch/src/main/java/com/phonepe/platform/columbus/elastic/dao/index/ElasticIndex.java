package com.phonepe.platform.columbus.elastic.dao.index;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Representation of {@link com.phonepe.platform.columbus.dao.index.Index Index} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticIndex {

    @NonNull
    private final String name;
}
