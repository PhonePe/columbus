package com.phonepe.commons.columbus.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.Filter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that matches documents where the child filter does NOT match (logical NOT)")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(NotFilter.TYPE)
public class NotFilter implements Filter {

    public static final String TYPE = "NOT";

    @JsonPropertyDescription("The filter to negate")
    private Filter filter;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
