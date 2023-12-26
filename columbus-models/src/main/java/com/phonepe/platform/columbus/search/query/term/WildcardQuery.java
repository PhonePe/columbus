package com.phonepe.platform.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(WildcardQuery.TYPE)
public class WildcardQuery extends FieldQuery {

    public static final String TYPE = "WILDCARD";

    private String value;

    private Boolean caseInsensitive;
    private String rewrite;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
