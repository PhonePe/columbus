package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BoostingQuery.TYPE)
public class BoostingQuery extends AbstractQuery {

    public static final String TYPE = "BOOSTING";

    private Query positive;

    private Query negative;

    @Builder.Default
    private float negativeBoost = 1f;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
