package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Boolean query combining multiple query clauses with logical operators (must, should, must_not, filter)")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BoolQuery.TYPE)
public class BoolQuery extends AbstractQuery {

    public static final String TYPE = "BOOL";

    @JsonPropertyDescription("Clauses that must match for a document to be returned. Contributes to scoring")
    @JsonAlias("must")
    @Singular(value = "must", ignoreNullCollections = true)
    private List<@NonNull Query> mustClauses;

    @JsonPropertyDescription("Clauses where at least one should match. Contributes to scoring")
    @JsonAlias("should")
    @Singular(value = "should", ignoreNullCollections = true)
    private List<@NonNull Query> shouldClauses;

    @JsonPropertyDescription("Clauses that must match but do not contribute to scoring. Used for filtering")
    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Clauses that must not match for a document to be returned")
    @JsonAlias({"mustNot", "must_not"})
    @Singular(value = "mustNot", ignoreNullCollections = true)
    private List<@NonNull Query> mustNotClauses;

    @JsonPropertyDescription("Minimum number of should clauses that must match. Can be a number or percentage")
    private String minimumMatch;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
