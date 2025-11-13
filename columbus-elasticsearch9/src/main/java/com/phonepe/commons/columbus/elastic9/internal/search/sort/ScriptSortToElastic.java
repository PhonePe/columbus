package com.phonepe.commons.columbus.elastic9.internal.search.sort;

import co.elastic.clients.elasticsearch._types.ScriptSort.Builder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.commons.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.sort.ScriptSort;

import java.util.Objects;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = ScriptSort.TYPE, type = ScriptSort.class)
public class ScriptSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<ScriptSort, C> {

    @Override
    public SortOptions visit(final ScriptSort sort, final Object props, final C context) {
        final var script = Objects.requireNonNull(sort.getScript(), "script must not be null");
        final var valueType = Objects.requireNonNull(sort.getValueType(), "valueType must not be null");

        final var builder = new Builder()
                .script(MiscTransforms.toElastic(script))
                .type(SearchEnums.toScriptSortType(valueType));

        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::mode);

        return new SortOptions.Builder().script(builder.build()).build();
    }
}
