package com.phonepe.platform.columbus.core.search.resolver;

import com.phonepe.platform.columbus.models.core.search.query.sorter.SorterSelection;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;

import java.util.List;
import java.util.Optional;

public interface SorterResolver {
    Optional<List<Sorter>> resolve(SorterSelection selection);
}
