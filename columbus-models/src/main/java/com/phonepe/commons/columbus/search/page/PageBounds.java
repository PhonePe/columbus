package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * Represents a page of values.
 */
@JsonClassDescription("Interface for defining pagination boundaries to retrieve subsets of search results")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(PageNumber.class),
        @JsonSubTypes.Type(PageOffset.class),
        @JsonSubTypes.Type(PagePointer.class)
})
public interface PageBounds {

    @NonNull
    @JsonPropertyDescription("The type identifier for this pagination strategy")
    String getType();

    @JsonPropertyDescription("Maximum number of results to return in this page")
    Integer getSize();
}
