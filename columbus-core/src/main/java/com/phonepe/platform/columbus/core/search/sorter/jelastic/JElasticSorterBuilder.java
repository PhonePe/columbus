package com.phonepe.platform.columbus.core.search.sorter.jelastic;

import com.phonepe.platform.columbus.core.search.sorter.BaseSorterBuilder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;

public interface JElasticSorterBuilder<T extends Sorter>
    extends BaseSorterBuilder<com.grookage.jelastic.core.models.query.sorter.Sorter, T, JElasticSorterBuilderContext> {
}
