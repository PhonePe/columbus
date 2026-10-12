package com.phonepe.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A score function defines how the scores are calculated for the matching documents.
 */
@JsonClassDescription("Interface for score functions that modify document relevance scores in function_score queries")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(WeightScore.class),
        @JsonSubTypes.Type(FieldScore.class),
        @JsonSubTypes.Type(RandomScore.class),
        @JsonSubTypes.Type(ScriptScore.class),
        @JsonSubTypes.Type(DecayScore.class)
})
public interface ScoreFunction {

    @JsonPropertyDescription("The type identifier for this score function")
    @NonNull
    String getType();
}
