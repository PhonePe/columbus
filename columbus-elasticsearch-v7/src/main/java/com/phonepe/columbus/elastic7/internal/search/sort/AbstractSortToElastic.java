package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic7.search.sort.SortToElastic;
import com.phonepe.columbus.search.sort.AbstractSort;
import org.elasticsearch.search.sort.SortBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

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
