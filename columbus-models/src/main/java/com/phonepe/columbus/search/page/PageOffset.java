package com.phonepe.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Offset-based pagination using zero-based offset from the start of results")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PageOffset.TYPE)
public class PageOffset extends AbstractPageBounds {

    public static final String TYPE = "OFFSET";

    @JsonPropertyDescription("The zero-based offset from which to start retrieving results")
    private Integer offset;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
