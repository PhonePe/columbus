package com.phonepe.platform.columbus.elastic.search.sort;

import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.sort.visitor.SortTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * The context used to transform {@link com.phonepe.platform.columbus.search.sort.Sort Sorters} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticSortTransformationContext<C extends ElasticSortTransformationContext<C>>
        extends SortTransformationContext<SortBuilder<?>, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
