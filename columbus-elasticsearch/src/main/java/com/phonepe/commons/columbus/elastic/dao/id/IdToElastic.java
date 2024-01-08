package com.phonepe.commons.columbus.elastic.dao.id;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.id.visitor.IdResolutionVisitor;

/**
 * An interface to resolve a specific {@link Id} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Id} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface IdToElastic<T extends Id, C extends ElasticIdResolutionContext<C>>
        extends IdResolutionVisitor<ElasticId, T, Object, C> {
}
