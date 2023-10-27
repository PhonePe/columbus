package com.phonepe.platform.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(GreaterThanEqualsFilter.TYPE)
public class GreaterThanEqualsFilter extends RangeFilter {

    public static final String TYPE = "GREATER_THAN_EQUALS";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
