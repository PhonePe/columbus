package com.phonepe.columbus.elastic7.search.sort;

import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.sort.visitor.SortTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * The context used to transform {@link com.phonepe.columbus.search.sort.Sort Sorters} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticSortTransformationContext<C extends ElasticSortTransformationContext<C>>
        extends SortTransformationContext<SortBuilder<?>, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
