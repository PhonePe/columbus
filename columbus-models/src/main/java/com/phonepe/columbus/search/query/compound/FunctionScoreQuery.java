package com.phonepe.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.AbstractQuery;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.score.ScoreFunction;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Query that allows modification of document scores using custom scoring functions")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FunctionScoreQuery.TYPE)
public class FunctionScoreQuery extends AbstractQuery {

    public static final String TYPE = "FUNCTION_SCORE";

    @JsonPropertyDescription("The base query to match documents")
    private Query query;

    @JsonPropertyDescription("List of score functions to apply to matched documents")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull ScoreFunction> functions;

    @JsonPropertyDescription("Maximum boost value that can be applied")
    private Float maxBoost;

    @JsonPropertyDescription("Minimum score threshold - documents with lower scores are excluded")
    private Float minScore;

    @JsonPropertyDescription("How function scores are combined with the query score (multiply, replace, sum, avg, max, min)")
    private String boostMode;

    @JsonPropertyDescription("How multiple function scores are combined (multiply, sum, avg, first, max, min)")
    private String scoreMode;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
