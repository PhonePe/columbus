package com.phonepe.commons.columbus.search.filter.string;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TextMatchFilter.TYPE)
public class TextMatchFilter extends FieldFilter {

    public static final String TYPE = "TEXT_MATCH";

    private String value;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
