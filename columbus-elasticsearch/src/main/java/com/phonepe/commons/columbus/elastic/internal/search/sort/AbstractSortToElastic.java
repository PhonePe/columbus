package com.phonepe.commons.columbus.elastic.internal.search.sort;

import com.phonepe.commons.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.commons.columbus.elastic.search.sort.SortToElastic;
import com.phonepe.commons.columbus.search.sort.AbstractSort;
import org.elasticsearch.search.sort.SortBuilder;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractSortToElastic<T extends AbstractSort, C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<T, C> {

    @Override
    public SortBuilder<?> visit(final T sort, final Object props, final C context) {
        final var builder = buildSort(sort, props, context);
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return builder;
    }

    @SuppressWarnings("java:S1452")
    protected abstract SortBuilder<?> buildSort(final T sort, final Object props, final C context);
}
