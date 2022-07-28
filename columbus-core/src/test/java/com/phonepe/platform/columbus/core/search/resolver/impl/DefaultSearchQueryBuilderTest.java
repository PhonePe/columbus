package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.ColumbusResolver;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DefaultSearchQueryBuilderTest {
    @Mock
    ColumbusResolver columbusResolver;

    DefaultSearchQueryBuilder defaultSearchQueryBuilder;

    private final SorterSelection sorterSelection1 = UnidirectionalSorterSelection.builder()
        .sorterName("#+price")
        .build();
    private final SorterSelection sorterSelection2 = UnidirectionalSorterSelection.builder()
        .sorterName("#+relevance")
        .build();
    private final FilterSelection filterSelection1 = FilterSelection.builder()
        .filterName("#=:price")
        .value(Value.exact(100))
        .build();
    private final FilterSelection filterSelection2 = FilterSelection.builder()
        .filterName("#<:deliveryDays")
        .value(Value.exact(10))
        .build();
    private final Filter filter1 = EqualsFilter.builder()
        .field("price")
        .value(Value.exact(100))
        .build();

    private final Sorter sorter1 = FieldSorter.builder()
        .field("price")
        .order(SortOrder.ASC)
        .build();
    private final Sorter sorter2 = FieldSorter.builder()
        .field("relevance")
        .order(SortOrder.ASC)
        .build();

    @BeforeEach
    void setUp() {
        defaultSearchQueryBuilder = new DefaultSearchQueryBuilder(columbusResolver);
        lenient().when(columbusResolver.resolveSorter(sorterSelection1)).thenReturn(List.of(sorter1));
        lenient().when(columbusResolver.resolveSorter(sorterSelection2)).thenReturn(List.of(sorter2));
        lenient().when(columbusResolver.resolveFilter(filterSelection1)).thenReturn(ResolvedFilter.builder().filter(filter1).build());
        lenient().when(columbusResolver.resolveFilter(filterSelection2)).thenReturn(ResolvedFilter.builder().build());
    }

    @Test
    void testBuildFilter() {
        List<FilterSelection> filterSelectionList = List.of(filterSelection1, filterSelection2);
        Filter filter = GreaterThanFilter.builder()
            .field("rating")
            .value(Value.exact(4))
            .build();

        ResolvedFilter resolvedFilter = defaultSearchQueryBuilder.buildFilter(filterSelectionList, filter);

        Filter resFilter = resolvedFilter.getFilter();
        assertTrue(resFilter instanceof AndFilter);
        assertEquals(2, ((AndFilter) resFilter).getFilters().size());
        assertEquals(((AndFilter) resFilter).getFilters().get(0), filter1);
        assertEquals(((AndFilter) resFilter).getFilters().get(1), filter);
    }

    @Test
    void testBuildWithoutQueryFilter() {
        List<FilterSelection> filterSelectionList = List.of(filterSelection1);

        ResolvedFilter resolvedFilter = defaultSearchQueryBuilder.buildFilter(filterSelectionList, null);

        Filter resFilter = resolvedFilter.getFilter();
        assertTrue(resFilter instanceof AndFilter);
        assertEquals(1, ((AndFilter) resFilter).getFilters().size());
        assertEquals(((AndFilter) resFilter).getFilters().get(0), filter1);
    }

    @Test
    void testBuildSorter() {
        List<Sorter> sorterList = defaultSearchQueryBuilder.buildSorters(List.of(sorterSelection1, sorterSelection2));

        assertEquals(2, sorterList.size());
        assertEquals(sorter1, sorterList.get(0));
        assertEquals(sorter2, sorterList.get(1));
    }
}