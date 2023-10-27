package com.phonepe.platform.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(LessThanFilter.TYPE)
public class LessThanFilter extends RangeFilter {

    public static final String TYPE = "LESS_THAN";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
