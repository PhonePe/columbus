package com.phonepe.commons.columbus.search.query.simple;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches all documents in the index")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchAllQuery.TYPE)
public class MatchAllQuery extends AbstractQuery {

    public static final String TYPE = "MATCH_ALL";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
