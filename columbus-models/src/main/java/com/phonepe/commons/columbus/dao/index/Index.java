package com.phonepe.commons.columbus.dao.index;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * An interface representing an index (or table) of entities.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Index {

    @NonNull
    String getType();
}
