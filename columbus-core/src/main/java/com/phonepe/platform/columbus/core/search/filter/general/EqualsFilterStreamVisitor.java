package com.phonepe.platform.columbus.core.search.filter.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamContext;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;

import java.util.stream.Stream;

@JsonTypeName(EqualsFilter.TYPE)
public class EqualsFilterStreamVisitor<T> extends FilterStreamVisitor<EqualsFilter<T>> {
    @Override
    public Stream<Filter> visit(EqualsFilter<T> filter, FilterStreamContext context) {
        return of(filter);
    }
}
