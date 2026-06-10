package com.phonepe.commons.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.misc.text.Fuzziness;
import com.phonepe.commons.columbus.search.misc.text.Terms;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Full-text query that analyzes the text and finds documents matching the analyzed terms")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchQuery.TYPE)
public class MatchQuery extends FieldQuery {

    public static final String TYPE = "MATCH";

    @JsonPropertyDescription("The text value to search for")
    private Object value;

    @JsonPropertyDescription("Text analysis settings including analyzer and operator")
    private Analysis analysis;

    @JsonPropertyDescription("Term matching settings like minimum should match")
    private Terms terms;

    @JsonPropertyDescription("Fuzzy matching configuration for handling typos")
    private Fuzziness fuzziness;

    @JsonPropertyDescription("Whether to ignore errors during query execution")
    private Boolean ignoreErrors;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
