package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.core.search.resolver.models.ResolvedFilter;
import com.phonepe.platform.columbus.models.core.search.query.FilterSelection;

import java.util.Optional;

public interface FilterResolver {
    Optional<ResolvedFilter> resolve(FilterSelection selection);
}
