package com.phonepe.commons.columbus.search.filter.general;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that matches documents where the field value exactly equals the specified value")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(EqualsFilter.TYPE)
public class EqualsFilter extends FieldFilter {

    public static final String TYPE = "EQUALS";

    @JsonPropertyDescription("The exact value to match")
    private Object value;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
