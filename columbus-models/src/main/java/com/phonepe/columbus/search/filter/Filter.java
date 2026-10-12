package com.phonepe.columbus.search.filter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.columbus.search.query.Query;

/**
 * A filter is a simplified {@link Query} for easier use, but minimal customization.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Filter extends Query {
}
