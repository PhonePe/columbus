package com.phonepe.platform.columbus.search.filter.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(InFilter.TYPE)
public class InFilter extends FieldFilter {

    public static final String TYPE = "IN";

    @Singular(ignoreNullCollections = true)
    private List<Object> values;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
