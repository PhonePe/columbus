package com.phonepe.columbus.search.filter.string;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that performs full-text matching on a text field")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TextMatchFilter.TYPE)
public class TextMatchFilter extends FieldFilter {

    public static final String TYPE = "TEXT_MATCH";

    @JsonPropertyDescription("The text value to search for")
    private String value;

    @Override
    @JsonPropertyDescription("The type identifier for this query, which is mandatory")
    public @NonNull String getType() {
        return TYPE;
    }
}
