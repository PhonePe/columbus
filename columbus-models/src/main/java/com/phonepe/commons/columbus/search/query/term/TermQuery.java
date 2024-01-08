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
@JsonTypeName(TermQuery.TYPE)
public class TermQuery extends FieldQuery {

    public static final String TYPE = "TERM";

    private Object value;

    private Boolean caseInsensitive;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
