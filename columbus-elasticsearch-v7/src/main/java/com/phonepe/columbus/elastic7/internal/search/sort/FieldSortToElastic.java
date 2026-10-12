package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.FieldSort;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldSort.TYPE, type = FieldSort.class)
public class FieldSortToElastic<C extends ElasticSortTransformationContext<C>> extends
        AbstractSortToElastic<FieldSort, C> {

    @Override
    protected SortBuilder<?> buildSort(final FieldSort sort, final Object props, final C context) {
        final var builder = SortBuilders.fieldSort(sort.getField());
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::sortMode);
        applyIfNotNull(sort.getNumericType(), builder::setNumericType);
        applyIfNotNull(sort.getFallbackFieldType(), builder::unmappedType);
        applyIfNotNull(sort.getMissingValue(), builder::missing);
        return builder;
    }
}
