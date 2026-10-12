package com.phonepe.columbus.elastic9.internal.search.sort;

import co.elastic.clients.elasticsearch._types.ScoreSort.Builder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.columbus.elastic9.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.sort.SortToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.ScoreSort;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = ScoreSort.TYPE, type = ScoreSort.class)
public class ScoreSortToElastic<C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<ScoreSort, C> {

    @Override
    public SortOptions visit(final ScoreSort sort, final Object props, final C context) {
        final var builder = new Builder();
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return new SortOptions.Builder().score(builder.build()).build();
    }
}
