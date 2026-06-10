package com.phonepe.commons.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents where the field value starts with a specified prefix")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PrefixQuery.TYPE)
public class PrefixQuery extends FieldQuery {

    public static final String TYPE = "PREFIX";

    @JsonPropertyDescription("The prefix string to match")
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
