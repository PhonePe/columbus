package com.phonepe.commons.columbus.elastic.internal.search.sort;

import com.phonepe.commons.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.sort.ScoreSort;
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
