package com.phonepe.commons.columbus.dao.id.visitor;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.id.IdResolver;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link IdResolver} that uses visitor to convert {@link Id} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class IdResolutionContext<R, P, C extends IdResolutionContext<R, P, C>>
        extends VisitorContext<R, Id, P, C> implements IdResolver<R, P> {

    @Override
    public R resolve(final Id id, final P context) {
        return visit(id, context);
    }

    @Override
    public R resolve(final Id id) {
        return visit(id);
    }
}
