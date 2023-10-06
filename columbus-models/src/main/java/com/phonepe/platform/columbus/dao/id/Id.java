package com.phonepe.platform.columbus.dao.id;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * An interface representing an entity's (unique) identifier.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Id {

    @NonNull
    String getType();
}
