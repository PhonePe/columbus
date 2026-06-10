package com.phonepe.commons.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents containing an exact phrase, with terms in the specified order")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchPhraseQuery.TYPE)
public class MatchPhraseQuery extends FieldQuery {

    public static final String TYPE = "MATCH_PHRASE";

    @JsonPropertyDescription("The phrase to search for")
    private Object value;

    @JsonPropertyDescription("Text analysis settings including analyzer")
    private Analysis analysis;

    @JsonPropertyDescription("Maximum number of positions between matching terms (allows for flexibility in phrase matching)")
    private Integer slop;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
