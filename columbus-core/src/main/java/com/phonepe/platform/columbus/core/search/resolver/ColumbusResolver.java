package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusCoreErrorCode;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ColumbusResolver {
    private final List<FilterResolver> filterResolvers;
    private final List<SorterResolver> sorterResolvers;

    public ResolvedFilter resolveFilter(FilterSelection selection) {
        return filterResolvers.stream()
            .map(filterResolver -> filterResolver.resolve(selection))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> {
                log.error("Failed to resolve filter: {}", selection);
                return ColumbusException.error(ColumbusCoreErrorCode.NO_RESOLVER_FOUND, Map.of(
                    "message", "Failed to resolve filter",
                    "filter", selection));
            });
    }

    public ResolvedSorter resolveSorter(SorterSelection selection) {
        return sorterResolvers.stream()
            .map(sorterResolver -> sorterResolver.resolve(selection))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst()
            .orElseThrow(() -> {
                log.error("Failed to resolve sorter: {}", selection);
                return ColumbusException.error(ColumbusCoreErrorCode.NO_RESOLVER_FOUND, Map.of(
                    "message", "Failed to resolve sorter",
                    "sorter", selection));
            });
    }
}
