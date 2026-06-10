package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that boosts documents matching the positive query while demoting those matching the negative query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BoostingQuery.TYPE)
public class BoostingQuery extends AbstractQuery {

    public static final String TYPE = "BOOSTING";

    @JsonPropertyDescription("Query for documents that should receive a positive boost")
    private Query positive;

    @JsonPropertyDescription("Query for documents that should have their scores reduced")
    private Query negative;

    @JsonPropertyDescription("Factor by which to multiply the score of documents matching the negative query (0.0 to 1.0)")
    @Builder.Default
    private float negativeBoost = 1f;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
