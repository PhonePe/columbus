package com.phonepe.platform.columbus.search.page.visitor;

import com.phonepe.platform.columbus.search.page.PageBounds;
import com.phonepe.platform.columbus.search.page.PageResolver;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link PageResolver} that uses visitor to convert {@link PageBounds} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class PageResolutionContext<R, C extends PageResolutionContext<R, C>>
        extends VisitorContext<R, PageBounds, C> implements PageResolver<R> {

    @Override
    public R resolve(final PageBounds page) {
        return visit(page);
    }
}
