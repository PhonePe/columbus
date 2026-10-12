package com.phonepe.columbus.elastic9.dao.id;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Representation of {@link com.phonepe.columbus.dao.id.Id Id} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticId {

    private final String id;
}
