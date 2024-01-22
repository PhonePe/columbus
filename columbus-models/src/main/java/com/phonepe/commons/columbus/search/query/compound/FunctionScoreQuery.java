package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.score.ScoreFunction;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FunctionScoreQuery.TYPE)
public class FunctionScoreQuery extends AbstractQuery {

    public static final String TYPE = "FUNCTION_SCORE";

    private Query query;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull ScoreFunction> functions;

    private Float maxBoost;
    private Float minScore;

    private String boostMode;
    private String scoreMode;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
