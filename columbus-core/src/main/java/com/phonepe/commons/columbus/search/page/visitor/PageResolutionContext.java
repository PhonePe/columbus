package com.phonepe.commons.columbus.search.page.visitor;

import com.phonepe.commons.columbus.search.page.PageBounds;
import com.phonepe.commons.columbus.search.page.PageResolver;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link PageResolver} that uses visitor to convert {@link PageBounds} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class PageResolutionContext<R, P, C extends PageResolutionContext<R, P, C>>
        extends VisitorContext<R, PageBounds, P, C> implements PageResolver<R, P> {

    @Override
    public R resolve(final PageBounds page, final P context) {
        return visit(page, context);
    }

    @Override
    public R resolve(final PageBounds page) {
        return visit(page);
    }
}
