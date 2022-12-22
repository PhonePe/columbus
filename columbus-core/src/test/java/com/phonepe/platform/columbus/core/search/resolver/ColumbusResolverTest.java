package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ColumbusResolverTest {
    ColumbusResolver columbusResolver;
    List<FilterResolver> filterResolvers;
    List<SorterResolver> sorterResolvers;
    @Mock
    FilterResolver filterResolver;
    @Mock
    SorterResolver sorterResolver;
    String filterName = "#>=:price";
    private final FilterSelection filterSelection = FilterSelection.builder()
        .filterName(filterName).value(Value.exact(200))
        .build();
    private final Filter filter = GreaterEqualsFilter.builder()
        .field("price").value(Value.exact(200))
        .build();
    private final ResolvedFilter resolvedFilter = ResolvedFilter.builder()
        .filter(filter).build();
    private final SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
        .sorterName("#+price")
        .build();
    private final Sorter sorter = FieldSorter.builder()
        .field("price").order(SortOrder.ASC)
        .build();
    private final ResolvedSorter resolvedSorter = ResolvedSorter.builder()
        .sorter(sorter)
        .build();

    @BeforeEach
    void setUp() {
        filterResolvers = new ArrayList<>();
        sorterResolvers = new ArrayList<>();
        lenient().when(filterResolver.resolve(filterSelection)).thenReturn(Optional.of(resolvedFilter));
        lenient().when(sorterResolver.resolve(sorterSelection)).thenReturn(Optional.of(resolvedSorter));
        lenient().when(filterResolver.resolve(filterSelection)).thenReturn(Optional.of(resolvedFilter));
        filterResolvers.add(filterResolver);
        sorterResolvers.add(sorterResolver);
        columbusResolver = new ColumbusResolver(filterResolvers, sorterResolvers);
    }

    @Test
    void testResolveFilterSuccess() {
        ResolvedFilter resolvedFilter = columbusResolver.resolveFilter(filterSelection);
        assertEquals(resolvedFilter, this.resolvedFilter);

    }

    @Test
    void testResolveFilterFail() {
        FilterSelection filterSelection1 = FilterSelection.builder()
            .filterName("abcd")
            .value(Value.exact(100))
            .build();
        ColumbusException exception = Assert.assertThrows(ColumbusException.class, () -> columbusResolver.resolveFilter(filterSelection1));
        assertEquals("Failed to resolve filter", exception.getContext().get("message"));
    }

    @Test
    void testResolveSorterSuccess() {
        List<Sorter> sorterList = columbusResolver.resolveSorter(sorterSelection).getSorters();
        assertEquals(1, sorterList.size());
        assertEquals(sorter, sorterList.get(0));
    }

    @Test
    void testResolveSorterFail() {
        SorterSelection sorterSelection1 = UnidirectionalSorterSelection.builder()
            .sorterName("#+modified")
            .build();
        ColumbusException exception = Assert.assertThrows(ColumbusException.class, () -> columbusResolver.resolveSorter(sorterSelection1));
        assertEquals("Failed to resolve sorter", exception.getContext().get("message"));
    }

}