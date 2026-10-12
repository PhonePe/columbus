package com.phonepe.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents containing an exact term without analysis. Useful for exact matches on keywords, IDs, or tags. Prefer MATCH_QUERY for fields that require text analysis.")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermQuery.TYPE)
public class TermQuery extends FieldQuery {

    public static final String TYPE = "TERM";

    @JsonPropertyDescription("The exact term value to match")
    private Object value;

    @JsonPropertyDescription("Whether to perform case-insensitive matching")
    private Boolean caseInsensitive;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
