package com.phonepe.platform.columbus.core.search.sorter.elastic;

import com.phonepe.platform.columbus.core.search.sorter.BaseSorterBuilder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import org.elasticsearch.search.sort.SortBuilder;

public interface ElasticSorterBuilder<T extends Sorter>
    extends BaseSorterBuilder<SortBuilder<?>, T, ElasticSorterBuilderContext> {
}
