package com.phonepe.platform.columbus.elastic.search.page;

import com.phonepe.platform.columbus.search.page.PageBounds;
import com.phonepe.platform.columbus.search.page.visitor.PageResolutionVisitor;

/**
 * An interface to resolve a specific {@link PageBounds} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link PageBounds} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface PageToElastic<T extends PageBounds, C extends ElasticPageResolutionContext<C>>
        extends PageResolutionVisitor<ElasticPage, T, Object, C> {
}
