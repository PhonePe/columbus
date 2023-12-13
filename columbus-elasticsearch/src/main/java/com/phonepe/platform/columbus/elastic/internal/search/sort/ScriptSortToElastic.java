package com.phonepe.platform.columbus.elastic.internal.search.sort;

import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.sort.ScriptSort;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import java.util.Objects;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = ScriptSort.TYPE, type = ScriptSort.class)
public class ScriptSortToElastic<C extends ElasticSortTransformationContext<C>>
        extends BaseSortToElastic<ScriptSort, C> {

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
