package com.phonepe.platform.columbus.visitor.impl;

import com.phonepe.platform.columbus.visitor.Visitor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * The visitor context includes all the data that is accessible while visiting a value. This allows passing
 * additional data to all the visitors, and support recursive execution.
 * <p>
 * It uses the {@link #resolver} to identify the visitor that is applicable for a given value and context type. Note
 * that the resolver need not be strictly bound to the type parameters of the context.
 *
 * @param <R> the return type of the visitor.
 * @param <B> the base type the visitor can visit.
 * @param <C> self reference, the context used by the visitor.
 * @see VisitorResolver
 * @see EntityVisitor
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class VisitorContext<R, B, C extends VisitorContext<R, B, C>> implements Visitor<R, B> {

    @NonNull
    private final VisitorResolver<? extends R, ? super B, ? super C> resolver;

    @Override
    public <T extends B> R visit(final T value) {
        return resolver.visitorFor(value).visit(value, self());
    }

    @NonNull
    protected abstract C self();
}
