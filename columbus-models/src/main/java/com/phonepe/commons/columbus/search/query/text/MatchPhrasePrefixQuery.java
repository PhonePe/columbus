package com.phonepe.commons.columbus.search.query.text;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.text.Analysis;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchPhrasePrefixQuery.TYPE)
public class MatchPhrasePrefixQuery extends FieldQuery {

    public static final String TYPE = "MATCH_PHRASE_PREFIX";

    private Object value;

    private Analysis analysis;
    private Integer slop;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
