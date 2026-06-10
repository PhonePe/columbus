package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that wraps another query and assigns a constant score to all matching documents, ignoring relevance")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ConstantScoreQuery.TYPE)
public class ConstantScoreQuery extends AbstractQuery {

    public static final String TYPE = "CONSTANT_SCORE";

    @JsonPropertyDescription("The query to execute. All matching documents will receive the same score")
    private Query query;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
