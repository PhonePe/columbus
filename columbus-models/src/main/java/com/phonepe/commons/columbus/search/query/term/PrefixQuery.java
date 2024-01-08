package com.phonepe.commons.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PrefixQuery.TYPE)
public class PrefixQuery extends FieldQuery {

    public static final String TYPE = "PREFIX";

    private String value;

    private Boolean caseInsensitive;
    private String rewrite;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
