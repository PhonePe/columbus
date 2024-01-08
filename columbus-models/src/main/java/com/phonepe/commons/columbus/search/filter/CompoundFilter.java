package com.phonepe.commons.columbus.search.filter;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public abstract class CompoundFilter implements Filter {

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Filter> filters;
}
