package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.ColumbusResolver;
import com.phonepe.platform.columbus.core.search.resolver.SearchQueryBuilder;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultSearchQueryBuilder implements SearchQueryBuilder {
    private final ColumbusResolver resolver;

    @Override
    public ResolvedFilter buildFilter(List<FilterSelection> selections, Filter queryFilter) {
        final var resolvedFilterBuilder = ResolvedFilter.builder();
        final var andFilterBuilder = AndFilter.builder();
        selections.stream().map(resolver::resolveFilter).forEachOrdered(resolvedFilter -> {
            if (resolvedFilter.getFilter() != null) {
                andFilterBuilder.filter(resolvedFilter.getFilter());
            }
            resolvedFilterBuilder.references(resolvedFilter.getReferences());
        });
        if (queryFilter != null) {
            andFilterBuilder.filter(queryFilter);
        }
        return resolvedFilterBuilder.filter(andFilterBuilder.build()).build();
    }

    @Override
    public ResolvedSorter buildSorters(List<SorterSelection> selections) {
        final var resolvedSorter = ResolvedSorter.builder();
        selections.stream()
            .map(resolver::resolveSorter)
            .forEachOrdered(sorter -> {
                resolvedSorter.sorters(sorter.getSorters());
                resolvedSorter.references(sorter.getReferences());
            });
        return resolvedSorter.build();
    }
}
