package com.phonepe.commons.columbus.elastic9.search.sort;

import co.elastic.clients.elasticsearch._types.SortOptions;
import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.search.sort.visitor.SortTransformationVisitor;

/**
 * An interface to transform a specific {@link Sort} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Sort} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface SortToElastic<T extends Sort, C extends ElasticSortTransformationContext<C>>
        extends SortTransformationVisitor<SortOptions, T, Object, C> {
}
