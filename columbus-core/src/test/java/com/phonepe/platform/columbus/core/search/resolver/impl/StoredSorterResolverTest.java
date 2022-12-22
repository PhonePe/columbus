package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.core.search.resolver.models.SorterEntry;
import com.phonepe.platform.columbus.core.search.value.ReferenceMapEntry;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StoredSorterResolverTest {
    private final Sorter relevanceSorter = FieldSorter.builder()
        .order(SortOrder.ASC)
        .field("relevance")
        .build();
    private final Sorter priceLowToHighSorter = FieldSorter.builder()
        .order(SortOrder.ASC)
        .field("price")
        .build();
    private final Sorter priceHighToLowSorter = FieldSorter.builder()
        .order(SortOrder.DESC)
        .field("price")
        .build();

    StoredSorterResolver storedSorterResolver;

    @BeforeEach
    void setUp() {
        Map<String, SorterEntry> storedSorters = new HashMap<>();
        storedSorters.put("relevance", sorterEntry(relevanceSorter));
        storedSorters.put("price", sorterEntry(priceLowToHighSorter));
        storedSorters.put("context", SorterEntry.builder()
            .sorter(relevanceSorter)
            .reference("reference")
            .build());
        storedSorterResolver = new StoredSorterResolver(storedSorters);
    }

    @Test
    void testUnidirectionalResolver() {
        SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
            .sorterName("relevance")
            .build();

        checkResolution(sorterSelection, List.of(relevanceSorter));
    }

    @Test
    void testBidirectionalResolver() {
        SorterSelection sorterSelection = BidirectionalSorterSelection.builder()
            .sorterName("price")
            .build();

        checkResolution(sorterSelection, List.of(priceLowToHighSorter));
    }

    @Test
    void testBidirectionalResolverDesc() {
        MapperUtils.initialise(new ObjectMapper());
        SorterSelection sorterSelection = BidirectionalSorterSelection.builder()
            .sorterName("price")
            .increasing(false)
            .build();

        checkResolution(sorterSelection, List.of(priceHighToLowSorter));
    }

    @Test
    void testResolveNotStored() {
        SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
            .sorterName("abc")
            .build();

        Optional<ResolvedSorter> resolvedSorter = storedSorterResolver.resolve(sorterSelection);

        assertTrue(resolvedSorter.isEmpty());
    }

    @Test
    void testSorterWithContext() {
        SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
            .sorterName("context")
            .context(Value.exact(10))
            .build();
        ReferenceMapEntry referenceMapEntry = ReferenceMapEntry.builder()
            .name("reference")
            .value(10)
            .build();

        Optional<ResolvedSorter> optionalResolvedSorter = storedSorterResolver.resolve(sorterSelection);

        assertTrue(optionalResolvedSorter.isPresent());
        var resolvedSorter = optionalResolvedSorter.get();
        assertEquals(List.of(relevanceSorter), resolvedSorter.getSorters());
        assertEquals(List.of(referenceMapEntry), resolvedSorter.getReferences());
    }

    private void checkResolution(SorterSelection sorterSelection, List<Sorter> sorters) {
        Optional<ResolvedSorter> resolvedSorter = storedSorterResolver.resolve(sorterSelection);

        assertTrue(resolvedSorter.isPresent());
        List<Sorter> sorterList = resolvedSorter.get().getSorters();
        assertEquals(sorters, sorterList);
    }

    private SorterEntry sorterEntry(final Sorter sorter) {
        return SorterEntry.builder()
            .sorter(sorter)
            .build();
    }
}