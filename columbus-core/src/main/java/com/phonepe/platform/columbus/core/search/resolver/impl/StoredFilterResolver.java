package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.FilterResolver;
import com.phonepe.platform.columbus.core.search.resolver.models.FilterEntry;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.value.ReferenceMapEntry;
import com.phonepe.platform.columbus.core.search.value.ValueGetter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.value.ReferencedValue;
import com.phonepe.platform.columbus.models.core.search.value.Value;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class StoredFilterResolver implements FilterResolver {
    private final Map<String, FilterEntry> storedFilters;

    @Override
    public Optional<ResolvedFilter> resolve(FilterSelection selection) {
        FilterEntry entry = storedFilters.get(selection.getFilterName());
        Value<Object> value = selection.getValue();
        if (entry == null || value instanceof ReferencedValue) {
            return Optional.empty();
        }
        return Optional.of(ResolvedFilter.builder()
            .filter(entry.getFilter())
            .reference(ReferenceMapEntry.builder()
                .name(entry.getReference())
                .value(value.accept(new ValueGetter<>()))
                .build())
            .build());
    }
}
