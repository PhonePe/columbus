package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.SorterResolver;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.core.search.resolver.models.SorterEntry;
import com.phonepe.platform.columbus.core.search.value.ReferenceMapEntry;
import com.phonepe.platform.columbus.core.search.value.ValueGetter;
import com.phonepe.platform.columbus.core.search.value.ValueIsEnclosed;
import com.phonepe.platform.columbus.core.utils.MapperUtils;
import com.phonepe.platform.columbus.models.core.search.query.sorter.BidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelectionVisitor;
import com.phonepe.platform.columbus.models.core.search.query.sorter.UnidirectionalSorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class StoredSorterResolver implements SorterResolver {
    private static final ValueGetter<Object> valueGetter = new ValueGetter<>();
    private static final ValueIsEnclosed<Object> valueIsEnclosed = new ValueIsEnclosed<>();

    private final Map<String, SorterEntry> storedSorters;

    @Override
    public Optional<ResolvedSorter> resolve(SorterSelection selection) {
        final SorterEntry entry = storedSorters.get(selection.getSorterName());
        if (entry == null) {
            return Optional.empty();
        }
        final var sorters = entry.getSorters();
        var builder = ResolvedSorter.builder()
            .sorters(selection.accept(new SorterSelectionVisitor<>() {
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
        if (entry.getReference() != null && hasValue(selection.getContext())) {
            builder.reference(ReferenceMapEntry.builder()
                .name(entry.getReference())
                .value(selection.getContext().accept(valueGetter))
                .build());
        }
        return Optional.of(builder.build());
    }

    private static boolean hasValue(Value<Object> value) {
        return value != null && value.accept(valueIsEnclosed);
    }
}
