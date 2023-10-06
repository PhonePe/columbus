package com.phonepe.platform.columbus.elastic.dao.id;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Representation of {@link com.phonepe.platform.columbus.dao.id.Id Id} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticId {

    private final String id;
}
