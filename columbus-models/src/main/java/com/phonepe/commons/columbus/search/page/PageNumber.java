package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Page-based pagination using page number (1-based indexing)")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PageNumber.TYPE)
public class PageNumber extends AbstractPageBounds {

    public static final String TYPE = "NUMBER";

    @JsonPropertyDescription("The page number to retrieve (1-based, first page is 1)")
    private Integer number;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
