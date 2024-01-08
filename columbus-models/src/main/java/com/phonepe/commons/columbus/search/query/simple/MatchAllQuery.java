package com.phonepe.commons.columbus.search.query.simple;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.BaseQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchAllQuery.TYPE)
public class MatchAllQuery extends BaseQuery {

    public static final String TYPE = "MATCH_ALL";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
