package com.phonepe.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that matches documents where the field value is less than the specified value")
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
