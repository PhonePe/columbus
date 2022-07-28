package com.phonepe.platform.columbus.core.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamContext;
import com.phonepe.platform.columbus.core.search.filter.FilterStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;

import java.util.stream.Stream;

@JsonTypeName(AndFilter.TYPE)
public class AndFilterStreamVisitor extends FilterStreamVisitor<AndFilter> {
    @Override
    public Stream<Filter> visit(AndFilter filter, FilterStreamContext context) {
        return valueAndRest(filter, filter.getFilters().stream().map(context::visit));
    }
}
