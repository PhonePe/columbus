package com.phonepe.commons.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A sort defines the order of the documents in the result.
 * <p>
 * The {@link #getOrder() order} can be changed to reverse the criteria.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Sort {

    @NonNull
    SortOrder getOrder();

    @NonNull
    String getType();
}
