package com.phonepe.columbus.search.filter.general;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Filter that matches documents where the field value is in the specified list of values")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(InFilter.TYPE)
public class InFilter extends FieldFilter {

    public static final String TYPE = "IN";

    @JsonPropertyDescription("List of values to match (documents matching any value will be returned)")
    @Singular(ignoreNullCollections = true)
    private List<Object> values;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
