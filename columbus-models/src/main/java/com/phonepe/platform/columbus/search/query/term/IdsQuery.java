package com.phonepe.platform.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.search.query.BaseQuery;
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
public class IdsQuery extends BaseQuery {

    public static final String TYPE = "IDS";

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Id> ids;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
