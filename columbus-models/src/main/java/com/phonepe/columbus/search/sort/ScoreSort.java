package com.phonepe.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Sort that orders documents based on their relevance score from the query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScoreSort.TYPE)
public class ScoreSort extends AbstractSort {

    public static final String TYPE = "SCORE";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
