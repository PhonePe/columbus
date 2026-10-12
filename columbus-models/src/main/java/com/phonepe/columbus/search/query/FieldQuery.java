package com.phonepe.columbus.search.query;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for field-specific queries that operate on a single field")
@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldQuery extends AbstractQuery {

    @JsonPropertyDescription("The name of the field to query")
    private String field;
}
