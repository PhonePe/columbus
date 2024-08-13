package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(DisMaxQuery.TYPE)
public class DisMaxQuery extends AbstractQuery {

    public static final String TYPE = "DIS_MAX";

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Query> queries;

    private Float tieBreaker;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
