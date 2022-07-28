package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.SorterResolver;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelectionVisitor;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class StoredSorterResolver implements SorterResolver {
    private final Map<String, List<Sorter>> storedSorters;

    @Override
    public Optional<List<Sorter>> resolve(SorterSelection selection) {
        final List<Sorter> sorters = storedSorters.get(selection.getSorterName());
        if (sorters == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(selection.accept(new SorterSelectionVisitor<>() {
            @Override
            public List<Sorter> visit(UnidirectionalSorterSelection unidirectional) {
                return sorters;
            }

            @Override
            @SuppressWarnings("java:S3958") // SonarQube fix
            public List<Sorter> visit(BidirectionalSorterSelection bidirectional) {
                return bidirectional.isIncreasing() ? sorters : sorters.stream().map(sorter -> {
                    Sorter copy = MapperUtils.copyValue(sorter);
                    copy.setOrder(copy.getOrder().flip());
                    return copy;
                }).toList();
            }
        }));
    }
}
