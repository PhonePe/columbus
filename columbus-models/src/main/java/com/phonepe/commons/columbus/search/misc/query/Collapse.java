package com.phonepe.commons.columbus.search.misc.query;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Selects the top result for each group formed based on the value in the field.
 */
@Data
@Builder
@Jacksonized
public class Collapse {

    private String field;

    /**
     * A set of queries to be run on each group.
     */
    @Singular(ignoreNullCollections = true)
    private List<@NonNull InnerQuery<?>> innerQueries;

    /**
     * The number of groups that can be queried in parallel at a time for inner queries.
     */
    private Integer parallelism;
}
