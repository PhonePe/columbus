package com.phonepe.platform.columbus.search.query.simple;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.query.BaseQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(MatchNoneQuery.TYPE)
public class MatchNoneQuery extends BaseQuery {

    public static final String TYPE = "MATCH_NONE";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
