package com.phonepe.platform.columbus.core.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamContext;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.OrFilter;

import java.util.stream.Stream;

@JsonTypeName(OrFilter.TYPE)
public class OrFilterStreamVisitor extends FilterStreamVisitor<OrFilter> {
    @Override
    public Stream<Filter> visit(OrFilter filter, FilterStreamContext context) {
        return valueAndRest(filter, filter.getFilters().stream().map(context::visit));
    }
}
