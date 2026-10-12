package com.phonepe.columbus.search.query.simple;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches no documents in the index")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchNoneQuery.TYPE)
public class MatchNoneQuery extends AbstractQuery {

    public static final String TYPE = "MATCH_NONE";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
