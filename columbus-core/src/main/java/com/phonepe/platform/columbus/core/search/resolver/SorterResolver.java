package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedSorter;
import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;

import java.util.Optional;

public interface SorterResolver {
    Optional<ResolvedSorter> resolve(SorterSelection selection);
}
