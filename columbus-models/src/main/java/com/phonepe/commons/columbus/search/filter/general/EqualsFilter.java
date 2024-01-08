package com.phonepe.commons.columbus.search.filter.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(EqualsFilter.TYPE)
public class EqualsFilter extends FieldFilter {

    public static final String TYPE = "EQUALS";

    private Object value;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
