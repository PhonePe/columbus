package com.phonepe.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.AbstractQuery;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Disjunction Max query that returns documents matching one or more queries, using the highest score plus tie breaker")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(DisMaxQuery.TYPE)
public class DisMaxQuery extends AbstractQuery {

    public static final String TYPE = "DIS_MAX";

    @JsonPropertyDescription("List of queries to execute. The highest score from matching queries will be used")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Query> queries;

    @JsonPropertyDescription("Tie breaker multiplier for scores from other matching queries (0.0 to 1.0)")
    private Float tieBreaker;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
