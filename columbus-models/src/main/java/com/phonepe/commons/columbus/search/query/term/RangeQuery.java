package com.phonepe.commons.columbus.search.query.term;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RangeQuery.TYPE)
public class RangeQuery extends FieldQuery {

    public static final String TYPE = "RANGE";

    private Object from;
    private Object to;

    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeLower = true;

    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeUpper = true;

    private String timezone;
    private String format;
    private String relation;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
