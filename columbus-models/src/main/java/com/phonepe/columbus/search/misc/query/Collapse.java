package com.phonepe.columbus.search.misc.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Selects the top result for each group formed based on the value in the field.")
@Data
@Builder
@Jacksonized
public class Collapse {

    @JsonPropertyDescription("The field on which to collapse the results")
    private String field;

    @JsonPropertyDescription("A set of queries to be run on each group")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull InnerQuery<?>> innerQueries;

    @JsonPropertyDescription("The number of groups that can be queried in parallel at a time for inner queries")
    private Integer parallelism;
}
