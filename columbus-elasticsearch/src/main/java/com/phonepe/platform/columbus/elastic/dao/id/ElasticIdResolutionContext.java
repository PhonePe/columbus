package com.phonepe.platform.columbus.elastic.dao.id;

import com.phonepe.platform.columbus.dao.id.visitor.IdResolutionContext;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve {@link com.phonepe.platform.columbus.dao.id.Id Id}s as needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@SuperBuilder(toBuilder = true)
public abstract class ElasticIdResolutionContext<C extends ElasticIdResolutionContext<C>>
        extends IdResolutionContext<ElasticId, Object, C> {
}
