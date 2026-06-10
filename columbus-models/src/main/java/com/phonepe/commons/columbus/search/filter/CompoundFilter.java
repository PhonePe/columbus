package com.phonepe.commons.columbus.search.filter;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@JsonClassDescription("Abstract base class for filters that combine multiple child filters")
@Data
@SuperBuilder(toBuilder = true)
public abstract class CompoundFilter implements Filter {

    @JsonPropertyDescription("List of child filters to combine")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Filter> filters;
}
