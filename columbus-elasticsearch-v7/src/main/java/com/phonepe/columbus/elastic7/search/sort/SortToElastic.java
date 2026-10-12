package com.phonepe.columbus.elastic7.search.sort;

import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.search.sort.visitor.SortTransformationVisitor;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * An interface to transform a specific {@link Sort} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Sort} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface SortToElastic<T extends Sort, C extends ElasticSortTransformationContext<C>>
        extends SortTransformationVisitor<SortBuilder<?>, T, Object, C> {
}
