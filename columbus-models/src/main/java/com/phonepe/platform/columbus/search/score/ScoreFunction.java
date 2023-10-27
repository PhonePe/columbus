package com.phonepe.platform.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A score function defines how the scores are calculated for the matching documents.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface ScoreFunction {

    @NonNull
    String getType();
}
