package com.phonepe.commons.columbus.search.aggregator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * An aggregator represents a way to summarize the documents by grouping them into buckets, applying mathematical
 * calculations, etc.
 * <p>
 * Each aggregator must be uniquely identified by its name.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Aggregator {

    @NonNull
    String getName();

    @NonNull
    String getType();
}
