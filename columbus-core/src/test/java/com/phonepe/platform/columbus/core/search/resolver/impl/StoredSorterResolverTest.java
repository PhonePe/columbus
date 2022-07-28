package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
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
        Map<String, List<Sorter>> storedSorters = new HashMap<>();
        storedSorters.put("relevance", List.of(relevanceSorter));
        storedSorters.put("price", List.of(priceLowToHighSorter));
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

        Optional<List<Sorter>> sorterListOptional = storedSorterResolver.resolve(sorterSelection);

        assertTrue(sorterListOptional.isEmpty());
    }

    private void checkResolution(SorterSelection sorterSelection, List<Sorter> sorters) {
        Optional<List<Sorter>> sorterListOptional = storedSorterResolver.resolve(sorterSelection);

        assertTrue(sorterListOptional.isPresent());
        List<Sorter> sorterList = sorterListOptional.get();
        assertEquals(sorters, sorterList);
    }
}