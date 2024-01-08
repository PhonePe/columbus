package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.BaseQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ConstantScoreQuery.TYPE)
public class ConstantScoreQuery extends BaseQuery {

    public static final String TYPE = "CONSTANT_SCORE";

    private Query query;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
