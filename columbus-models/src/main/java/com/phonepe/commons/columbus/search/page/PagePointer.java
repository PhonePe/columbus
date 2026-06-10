package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Cursor-based pagination using a pointer/token from a previous page for efficient deep pagination")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PagePointer.TYPE)
public class PagePointer extends AbstractPageBounds {

    public static final String TYPE = "POINTER";

    @JsonPropertyDescription("Opaque pointer/cursor token from the previous page to continue pagination from")
    private Object pointer;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
