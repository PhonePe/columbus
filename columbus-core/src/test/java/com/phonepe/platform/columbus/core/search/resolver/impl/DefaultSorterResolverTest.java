package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultSorterResolverTest {
    DefaultSorterResolver defaultSorterResolver;

    @BeforeEach
    void setUp() {
        defaultSorterResolver = new DefaultSorterResolver();
    }

    @Test
    void testResolveUnidirectionalSorter() {
        SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
            .sorterName("#+price")
            .build();
        Sorter sorter = FieldSorter.builder()
            .field("price")
            .order(SortOrder.ASC)
            .build();

        checkResolution(sorterSelection, sorter);
    }

    @Test
    void testResolveUnidirectionalSorterDesc() {
        SorterSelection sorterSelection = UnidirectionalSorterSelection.builder()
            .sorterName("#-price")
            .build();
        Sorter sorter = FieldSorter.builder()
            .field("price")
            .order(SortOrder.DESC)
            .build();

        checkResolution(sorterSelection, sorter);
    }

    @Test
    void testResolveBidirectionalSorter() {
        SorterSelection sorterSelection = BidirectionalSorterSelection.builder()
            .sorterName("#?price")
            .build();
        Sorter sorter = FieldSorter.builder()
            .field("price")
            .order(SortOrder.ASC)
            .build();

        checkResolution(sorterSelection, sorter);
    }

    @Test
    void testResolveBidirectionalSorterDesc() {
        SorterSelection sorterSelection = BidirectionalSorterSelection.builder()
            .sorterName("#?price")
            .increasing(false)
            .build();
        Sorter sorter = FieldSorter.builder()
            .field("price")
            .order(SortOrder.DESC)
            .build();

        checkResolution(sorterSelection, sorter);
    }

    @Test
    void testInvalidPrefix() {
        checkNoResolution(UnidirectionalSorterSelection.builder()
            .sorterName("a")
            .build());
    }

    @Test
    void testInvalidSorterName() {
        checkNoResolution(UnidirectionalSorterSelection.builder()
            .sorterName("#a")
            .build());
    }

    @Test
    void testInvalidUnidirectionalOrder() {
        checkNoResolution(UnidirectionalSorterSelection.builder()
            .sorterName("#abc")
            .build());
    }

    @Test
    void testInvalidBidirectionalOrder() {
        checkNoResolution(BidirectionalSorterSelection.builder()
            .sorterName("#+abc")
            .increasing(true)
            .build());
    }

    private void checkNoResolution(SorterSelection selection) {
        Optional<List<Sorter>> sorters = defaultSorterResolver.resolve(selection);

        assertTrue(sorters.isEmpty());
    }
    private void checkResolution(SorterSelection sorterSelection, Sorter sorter) {
        Optional<List<Sorter>> sorterListOptional = defaultSorterResolver.resolve(sorterSelection);

        assertTrue(sorterListOptional.isPresent());
        List<Sorter> sorterList = sorterListOptional.get();
        assertEquals(1, sorterList.size());
        assertEquals(sorter, sorterList.get(0));
    }
}