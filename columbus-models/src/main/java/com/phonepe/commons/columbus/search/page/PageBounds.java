package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * Represents a page of values.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface PageBounds {

    @NonNull
    String getType();

    Integer getSize();
}
