package com.phonepe.platform.columbus.dao.index.visitor;

import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.index.IndexResolver;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link IndexResolver} that uses visitor to convert {@link Index} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class IndexResolutionContext<R, C extends IndexResolutionContext<R, C>>
        extends VisitorContext<R, Index, C> implements IndexResolver<R> {

    @Override
    public R resolve(final Index index) {
        return visit(index);
    }
}
