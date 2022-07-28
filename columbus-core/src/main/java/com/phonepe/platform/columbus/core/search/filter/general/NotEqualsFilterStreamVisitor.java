package com.phonepe.platform.columbus.core.search.filter.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamContext;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;

import java.util.stream.Stream;

@JsonTypeName(NotEqualsFilter.TYPE)
public class NotEqualsFilterStreamVisitor<T> extends FilterStreamVisitor<NotEqualsFilter<T>> {
    @Override
    public Stream<Filter> visit(NotEqualsFilter<T> filter, FilterStreamContext context) {
        return of(filter);
    }
}
