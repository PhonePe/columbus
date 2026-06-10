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

@JsonClassDescription("Phrase query where the last term is treated as a prefix, useful for search-as-you-type")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchPhrasePrefixQuery.TYPE)
public class MatchPhrasePrefixQuery extends FieldQuery {

    public static final String TYPE = "MATCH_PHRASE_PREFIX";

    @JsonPropertyDescription("The phrase to search for, with the last term treated as a prefix")
    private Object value;

    @JsonPropertyDescription("Text analysis settings including analyzer")
    private Analysis analysis;

    @JsonPropertyDescription("Maximum number of positions between matching terms")
    private Integer slop;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
