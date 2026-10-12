package com.phonepe.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Query that matches documents by their unique document IDs")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(IdsQuery.TYPE)
public class IdsQuery extends AbstractQuery {

    public static final String TYPE = "IDS";

    @JsonPropertyDescription("List of document IDs to match")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Id> ids;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
