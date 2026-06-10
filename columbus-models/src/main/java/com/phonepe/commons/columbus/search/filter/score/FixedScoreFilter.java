package com.phonepe.commons.columbus.search.filter.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.Filter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter wrapper that assigns a fixed score boost to all matching documents")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FixedScoreFilter.TYPE)
public class FixedScoreFilter implements Filter {

    public static final String TYPE = "FIXED_SCORE";

    @JsonPropertyDescription("The filter to execute")
    private Filter filter;

    @JsonPropertyDescription("The fixed boost value to assign to matching documents")
    private Float boost;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
