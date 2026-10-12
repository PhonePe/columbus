package com.phonepe.columbus.elastic9.internal.search.sort;

import co.elastic.clients.elasticsearch._types.FieldSort.Builder;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.FieldSort;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldSort.TYPE, type = FieldSort.class)
public class FieldSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<FieldSort, C> {

    @Override
    public SortOptions visit(final FieldSort sort, final Object props, final C context) {
        final var builder = new Builder().field(sort.getField());
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::mode);
        applyIfNotNull(sort.getNumericType(), SearchEnums::toFieldSortNumericType, builder::numericType);
        applyIfNotNull(sort.getFallbackFieldType(), SearchEnums::toUnmappedType, builder::unmappedType);
        applyIfNotNull(sort.getMissingValue(), FieldValue::of, builder::missing);
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return new SortOptions.Builder().field(builder.build()).build();
    }
}
