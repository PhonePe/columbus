package com.phonepe.commons.columbus.search.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all query implementations providing common properties like name and boost")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractQuery implements Query {

    @JsonPropertyDescription("Optional name to identify this query in the response")
    private String name;

    @JsonPropertyDescription("Optional boost value to increase or decrease the relevance score of matching documents")
    private Float boost;
}
