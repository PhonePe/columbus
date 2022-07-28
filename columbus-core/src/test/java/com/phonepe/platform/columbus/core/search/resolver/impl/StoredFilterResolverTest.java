package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.models.FilterEntry;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.value.ReferenceMapEntry;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoredFilterResolverTest {
    private final Filter priceFilter = EqualsFilter.<Integer>builder()
        .field("price")
        .value(Value.referenced("$price"))
        .build();

    StoredFilterResolver storedFilterResolver;

    @BeforeEach
    void setUp() {
        Map<String, FilterEntry> storedFilters = new HashMap<>();
        storedFilters.put("priceFilter", FilterEntry.builder()
            .filter(priceFilter)
            .reference("$price").
            build());
        storedFilterResolver = new StoredFilterResolver(storedFilters);
    }

    @Test
    void testResolve() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("priceFilter")
            .value(Value.exact(1))
            .build();
        ReferenceMapEntry priceEntry = ReferenceMapEntry.builder()
            .name("$price")
            .value(1)
            .build();

        Optional<ResolvedFilter> resolvedFilterOptional = storedFilterResolver.resolve(filterSelection);

        assertTrue(resolvedFilterOptional.isPresent());
        ResolvedFilter resolvedFilter = resolvedFilterOptional.get();
        assertEquals(priceFilter, resolvedFilter.getFilter());
        assertEquals(1, resolvedFilter.getReferences().size());
        assertEquals(priceEntry, resolvedFilter.getReferences().get(0));
    }

    @Test
    void testResolveNotStored() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("otherFilter")
            .value(Value.exact(1))
            .build();

        checkNoResolution(filterSelection);
    }

    @Test
    void testResolveReference() {
        FilterSelection filterSelection = FilterSelection.builder()
            .filterName("priceFilter")
            .value(Value.referenced("name"))
            .build();

        checkNoResolution(filterSelection);
    }

    private void checkNoResolution(FilterSelection filterSelection) {
        Optional<ResolvedFilter> resolvedFilterOptional = storedFilterResolver.resolve(filterSelection);

        assertTrue(resolvedFilterOptional.isEmpty());
    }
}