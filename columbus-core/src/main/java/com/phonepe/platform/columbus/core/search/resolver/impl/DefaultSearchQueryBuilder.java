package com.phonepe.platform.columbus.core.search.resolver.impl;

import com.phonepe.platform.columbus.core.search.resolver.ColumbusResolver;
import com.phonepe.platform.columbus.core.search.resolver.SearchQueryBuilder;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@SuppressWarnings("java:S3958") // SonarQube fix
public class DefaultSearchQueryBuilder implements SearchQueryBuilder {
    private final ColumbusResolver resolver;

    @Override
    public ResolvedFilter buildFilter(List<FilterSelection> selections, Filter queryFilter) {
        val resolvedFilterBuilder = ResolvedFilter.builder();
        val andFilterBuilder = AndFilter.builder();
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
    public List<Sorter> buildSorters(List<SorterSelection> selections) {
        return selections.stream()
            .map(resolver::resolveSorter)
            .flatMap(Collection::stream)
            .toList();
    }
}
