package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.SorterResolver;
import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelectionVisitor;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.FieldSorter;
import com.phonepe.platform.columbus.models.core.search.sorter.SortOrder;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DefaultSorterResolver implements SorterResolver {
    @Override
    public Optional<List<Sorter>> resolve(SorterSelection selection) {
        String name = selection.getSorterName();
        // Resolves the sorters supported by Columbus: the names start with #
        if (name.startsWith("#")) {
            return Optional.ofNullable(parseFieldSorter(selection))
                .map(Collections::singletonList);
        }
        return Optional.empty();
    }

    private Sorter parseFieldSorter(SorterSelection sorterSelection) {
        // Field sorters are of the form <+/-/?><field>
        // +/- indicate order for unidirectional, and ? is a placeholder for bidirectional
        if (sorterSelection.getSorterName().length() < 3) {
            return null;
        }
        return sorterSelection.accept(new SorterSelectionVisitor<>() {
            @Override
            public Sorter visit(UnidirectionalSorterSelection selection) {
                String name = selection.getSorterName();
                char order = name.charAt(1);
                if (order != '+' && order != '-') {
                    return null;
                }
                return FieldSorter.builder()
                    .order(order == '+' ? SortOrder.ASC : SortOrder.DESC)
                    .field(name.substring(2))
                    .build();
            }

            @Override
            public Sorter visit(BidirectionalSorterSelection selection) {
                String name = selection.getSorterName();
                if (name.charAt(1) != '?') {
                    return null;
                }
                return FieldSorter.builder()
                    .order(selection.isIncreasing() ? SortOrder.ASC : SortOrder.DESC)
                    .field(name.substring(2))
                    .build();
            }
        });
    }
}
