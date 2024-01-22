package com.phonepe.commons.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(IdsQuery.TYPE)
public class IdsQuery extends AbstractQuery {

    public static final String TYPE = "IDS";

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Id> ids;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
