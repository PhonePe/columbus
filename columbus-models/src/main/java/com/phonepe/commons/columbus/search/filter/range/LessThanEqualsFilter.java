package com.phonepe.commons.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(LessThanEqualsFilter.TYPE)
public class LessThanEqualsFilter extends RangeFilter {

    public static final String TYPE = "LESS_THAN_EQUALS";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
