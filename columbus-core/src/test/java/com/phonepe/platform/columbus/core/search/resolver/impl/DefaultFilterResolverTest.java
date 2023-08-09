package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.general.WildCardFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.BetweenFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessEqualsFilter;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessThanFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.value.BetweenRange;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultFilterResolverTest {
    DefaultFilterResolver defaultFilterResolver;

    @BeforeEach
    void setUp() {
        defaultFilterResolver = new DefaultFilterResolver();
    }

    @Test
    void testResolveEquals() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#=:price")
            .value(Value.exact(1))
            .build();
        Filter filter = EqualsFilter.<Integer>builder()
            .field("price")
            .value(Value.exact(1))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveWildcard() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#wc:price")
            .value(Value.exact("price"))
            .build();
        Filter filter = WildCardFilter.builder()
            .field("price")
            .value(Value.exact("price"))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveNotEquals() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#!=:price")
            .value(Value.exact(1))
            .build();
        Filter filter = NotEqualsFilter.<Integer>builder()
            .field("price")
            .value(Value.exact(1))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveInFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#in:price")
            .value(Value.embedded(List.of(1, 2, 3)))
            .build();
        Filter filter = InFilter.<Integer>builder()
            .field("price")
            .values(Value.exact(List.of(1, 2, 3)))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveInFilterReference() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#in:price")
            .value(Value.referenced("inList"))
            .build();
        Filter filter = InFilter.<Integer>builder()
            .field("price")
            .values(Value.referenced("inList"))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveLessFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#<:price")
            .value(Value.exact(200))
            .build();
        Filter filter = LessThanFilter.builder()
            .field("price")
            .value(Value.exact(200))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveLessEqualFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#<=:price")
            .value(Value.exact(200))
            .build();
        Filter filter = LessEqualsFilter.builder()
            .field("price")
            .value(Value.exact(200))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveGreaterFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#>:price")
            .value(Value.exact(200))
            .build();
        Filter filter = GreaterThanFilter.builder()
            .field("price")
            .value(Value.exact(200))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveGreaterEqualFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#>=:price")
            .value(Value.exact(200))
            .build();
        Filter filter = GreaterEqualsFilter.builder()
            .field("price")
            .value(Value.exact(200))
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testResolveBetweenFilter() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("#<=:price:<=")
            .value(Value.exact(BetweenRange.builder()
                .from(Value.exact(1000))
                .to(Value.exact(2000))
                .build()))
            .build();
        Filter filter = BetweenFilter.builder()
            .field("price")
            .from(Value.exact(1000))
            .to(Value.exact(2000))
            .includeLower(true)
            .includeUpper(true)
            .build();

        checkResolution(filterSelection, filter);
    }

    @Test
    void testInvalidPrefix() {
        checkNoResolution(FilterSelection.builder()
            .filterName("abc")
            .value(Value.exact(1))
            .build());
    }

    @Test
    void testInvalidFieldFilter() {
        checkNoResolution(FilterSelection.builder()
            .filterName("#a")
            .value(Value.exact(1))
            .build());
    }

    @Test
    void testInvalidFieldFilterTwoSplits() {
        checkNoResolution(FilterSelection.builder()
            .filterName("#a:b")
            .value(Value.exact(1))
            .build());
    }

    @Test
    void testInvalidInFilter() {
        checkNoResolution(FilterSelection.builder()
            .filterName("#in:price")
            .value(Value.exact(1))
            .build());
    }

    @Test
    void testInvalidBetweenFilter() {
        checkNoResolution(FilterSelection.builder()
            .filterName("#<:price:<")
            .value(Value.exact(1))
            .build());
    }

    private void checkResolution(FilterSelection selection, Filter filter) {
        Optional<ResolvedFilter> resolvedFilterOptional = defaultFilterResolver.resolve(selection);

        assertTrue(resolvedFilterOptional.isPresent());

        ResolvedFilter resolvedFilter = resolvedFilterOptional.get();
        assertEquals(filter, resolvedFilter.getFilter());
        assertTrue(resolvedFilter.getReferences().isEmpty());
    }

    private void checkNoResolution(FilterSelection selection) {
        Optional<ResolvedFilter> resolvedFilterOptional = defaultFilterResolver.resolve(selection);

        assertTrue(resolvedFilterOptional.isEmpty());
    }
}