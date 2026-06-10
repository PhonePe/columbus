package com.phonepe.commons.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Query that matches documents where the field contains any of the specified terms")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermsQuery.TYPE)
public class TermsQuery extends FieldQuery {

    public static final String TYPE = "TERMS";

    @JsonPropertyDescription("List of exact term values to match (documents matching any term will be returned)")
    @Singular(ignoreNullCollections = true)
    private List<Object> values;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
