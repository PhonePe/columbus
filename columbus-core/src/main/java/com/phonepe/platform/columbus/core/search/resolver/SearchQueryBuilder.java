package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;

import java.util.List;

public interface SearchQueryBuilder {
    ResolvedFilter buildFilter(List<FilterSelection> selections, Filter queryFilter);

    List<Sorter> buildSorters(List<SorterSelection> selections);
}
