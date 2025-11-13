package com.phonepe.commons.columbus.elastic9.search.sort;

import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.sort.visitor.SortTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to transform {@link com.phonepe.commons.columbus.search.sort.Sort Sorters} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticSortTransformationContext<C extends ElasticSortTransformationContext<C>>
        extends SortTransformationContext<SortOptions, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
