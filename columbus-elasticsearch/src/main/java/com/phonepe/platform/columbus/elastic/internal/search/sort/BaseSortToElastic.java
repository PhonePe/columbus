package com.phonepe.platform.columbus.elastic.internal.search.sort;

import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.platform.columbus.elastic.search.sort.SortToElastic;
import com.phonepe.platform.columbus.search.sort.BaseSort;
import org.elasticsearch.search.sort.SortBuilder;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

public abstract class BaseSortToElastic<T extends BaseSort, C extends ElasticSortTransformationContext<C>>
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
