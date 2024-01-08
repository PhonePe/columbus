package com.phonepe.commons.columbus.elastic.search.page;

import com.phonepe.commons.columbus.search.page.visitor.PageResolutionContext;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve {@link com.phonepe.commons.columbus.search.page.PageBounds PageBounds} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@SuperBuilder(toBuilder = true)
public abstract class ElasticPageResolutionContext<C extends ElasticPageResolutionContext<C>>
        extends PageResolutionContext<ElasticPage, Object, C> {
}
