package com.phonepe.platform.columbus.dao.id.visitor;

import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.id.IdResolver;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link IdResolver} that uses visitor to convert {@link Id} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class IdResolutionContext<R, C extends IdResolutionContext<R, C>> extends VisitorContext<R, Id, C>
        implements IdResolver<R> {

    @Override
    public R resolve(final Id id) {
        return visit(id);
    }
}
