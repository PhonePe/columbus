package com.phonepe.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Query that matches documents where the specified field exists and has a non-null value")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FieldExistsQuery.TYPE)
public class FieldExistsQuery extends FieldQuery {

    public static final String TYPE = "FIELD_EXISTS";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
