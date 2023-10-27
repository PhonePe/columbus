package com.phonepe.platform.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermsQuery.TYPE)
public class TermsQuery extends FieldQuery {

    public static final String TYPE = "TERMS";

    @Singular(ignoreNullCollections = true)
    private List<Object> values;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
