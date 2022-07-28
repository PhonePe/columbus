package com.phonepe.platform.columbus.core.search.filter.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamContext;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;

import java.util.stream.Stream;

@JsonTypeName(InFilter.TYPE)
public class InFilterStreamVisitor<T> extends FilterStreamVisitor<InFilter<T>> {
    @Override
    public Stream<Filter> visit(InFilter<T> filter, FilterStreamContext context) {
        return of(filter);
    }
}
