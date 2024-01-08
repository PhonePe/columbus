package com.phonepe.commons.columbus.search.query;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A query represents a certain criteria to select a subset of data. This can be a simple leaf query, or a complex
 * nested query.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Query {

    @NonNull
    String getType();
}
