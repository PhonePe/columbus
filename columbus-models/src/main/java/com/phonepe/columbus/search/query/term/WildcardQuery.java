package com.phonepe.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents using wildcard patterns (* matches any sequence, ? matches any single character)")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(WildcardQuery.TYPE)
public class WildcardQuery extends FieldQuery {

    public static final String TYPE = "WILDCARD";

    @JsonPropertyDescription("Wildcard pattern to match (use * for any sequence, ? for any single character)")
    private String value;

    @JsonPropertyDescription("Whether to perform case-insensitive matching")
    private Boolean caseInsensitive;

    @JsonPropertyDescription("Method used to rewrite the query (constant_score, scoring_boolean, etc.)")
    private String rewrite;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
