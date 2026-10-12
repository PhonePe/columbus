package com.phonepe.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all score function implementations providing common weight and filter properties")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractScoreFunction implements ScoreFunction {

    @JsonPropertyDescription("Optional weight multiplier to apply to the score computed by this function")
    private Float weight;

    @JsonPropertyDescription("Optional filter query - this function only applies to documents matching this filter")
    private Query query;
}
