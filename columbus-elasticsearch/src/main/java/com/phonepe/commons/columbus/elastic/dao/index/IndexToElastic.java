package com.phonepe.commons.columbus.elastic.dao.index;

import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.visitor.IndexResolutionVisitor;

/**
 * An interface to resolve a specific {@link Index} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Index} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface IndexToElastic<T extends Index, C extends ElasticIndexResolutionContext<C>>
        extends IndexResolutionVisitor<ElasticIndex, T, Object, C> {
}
