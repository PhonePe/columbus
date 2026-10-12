package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.sort.ScoreSort;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

@RegistryKey(name = ScoreSort.TYPE, type = ScoreSort.class)
public class ScoreSortToElastic<C extends ElasticSortTransformationContext<C>> extends
        AbstractSortToElastic<ScoreSort, C> {

    @Override
    protected SortBuilder<?> buildSort(final ScoreSort sort, final Object props, final C context) {
        return SortBuilders.scoreSort();
    }
}
