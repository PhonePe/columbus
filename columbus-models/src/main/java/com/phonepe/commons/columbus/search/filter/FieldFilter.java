package com.phonepe.commons.columbus.search.filter;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for filters that operate on a single field")
@Data
@SuperBuilder(toBuilder = true)
public abstract class FieldFilter implements Filter {

    @JsonPropertyDescription("The name of the field to filter on")
    private String field;
}
