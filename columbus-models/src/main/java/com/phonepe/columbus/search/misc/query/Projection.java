package com.phonepe.columbus.search.misc.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Defines which fields to fetch in the result.")
@Data
@Builder
@Jacksonized
public class Projection {

    @JsonPropertyDescription("Whether to disable the projection and fetch all fields.")
    @Accessors(fluent = true)
    private boolean disabled;

    @JsonPropertyDescription("List of fields to include in the result.")
    @Singular(ignoreNullCollections = true)
    private List<String> includeFields;

    @JsonPropertyDescription("List of fields to exclude from the result.")
    @Singular(ignoreNullCollections = true)
    private List<String> excludeFields;
}
