package com.phonepe.platform.columbus.elastic.search.page;

import com.phonepe.platform.columbus.search.page.visitor.PageResolutionContext;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve {@link com.phonepe.platform.columbus.search.page.PageBounds PageBounds} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@SuperBuilder(toBuilder = true)
public abstract class ElasticPageResolutionContext<C extends ElasticPageResolutionContext<C>>
        extends PageResolutionContext<ElasticPage, C> {
}
