package com.phonepe.commons.columbus.elastic9.dao.id;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Representation of {@link com.phonepe.commons.columbus.dao.id.Id Id} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticId {

    private final String id;
}
