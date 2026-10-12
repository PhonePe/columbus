package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.ScriptSort;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = ScriptSort.TYPE, type = ScriptSort.class)
public class ScriptSortToElastic<C extends ElasticSortTransformationContext<C>>
        extends AbstractSortToElastic<ScriptSort, C> {

    @Override
    protected SortBuilder<?> buildSort(final ScriptSort sort, final Object props, final C context) {
        final var script = Objects.requireNonNull(sort.getScript(), "script must not be null");
        final var valueType = Objects.requireNonNull(sort.getValueType(), "valueType must not be null");
        final var builder = SortBuilders.scriptSort(
                MiscTransforms.toElastic(script),
                SearchEnums.toScriptSortType(valueType));
        applyIfNotNull(sort.getSortMode(), SearchEnums::toSortMode, builder::sortMode);
        return builder;
    }
}
