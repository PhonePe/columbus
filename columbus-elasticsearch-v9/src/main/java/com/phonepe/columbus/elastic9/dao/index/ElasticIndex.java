package com.phonepe.columbus.elastic9.dao.index;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Representation of {@link com.phonepe.columbus.dao.index.Index Index} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticIndex {

    @NonNull
    private final String name;
}
