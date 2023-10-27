package com.phonepe.platform.columbus.elastic.internal.search.sort;

import com.phonepe.platform.columbus.elastic.search.sort.ElasticSortTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.sort.ScoreSort;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

@RegistryKey(name = ScoreSort.TYPE, type = ScoreSort.class)
public class ScoreSortToElastic<C extends ElasticSortTransformationContext<C>> extends BaseSortToElastic<ScoreSort, C> {

    @Override
    protected SortBuilder<?> buildSort(final ScoreSort sort, final C context) {
        return SortBuilders.scoreSort();
    }
}
