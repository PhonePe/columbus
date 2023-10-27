package com.phonepe.platform.columbus.search.misc.query;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Defines which fields to fetch in the result.
 */
@Data
@Builder
@Jacksonized
public class Projection {

    @Accessors(fluent = true)
    private boolean disabled;

    @Singular(ignoreNullCollections = true)
    private List<String> includeFields;

    @Singular(ignoreNullCollections = true)
    private List<String> excludeFields;
}
