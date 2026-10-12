package com.phonepe.columbus.visitor.impl;

import lombok.NonNull;

/**
 * A resolver determines which {@link EntityVisitor} can be used for the given value.
 * <p>
 * The visitors need not be strictly bound to the type parameters of the resolver or the context. The type of context
 * is contravariant and the return type is covariant. So, all visitors that accept a context of type {@code C} or its
 * parent, and all visitors that return {@code R} or its children can be used.
 *
 * @param <R> the return type of the visitor.
 * @param <B> the base type the visitor can visit.
 * @param <P> the type of props the visitor supports.
 * @param <C> the type of the context the visitors can work with.
 * @see EntityVisitor
 */
public interface VisitorResolver<R, B, P, C extends VisitorContext<R, B, P, C>> {

    /**
     * Get the visitor for the given value.
     *
     * @param value the value being visited.
     * @param <T>   the type of the value being visited.
     * @return the visitor that can visit the given value.
     * @throws IllegalArgumentException if no visitor can visit the given value.
     * @throws NullPointerException     if value is null and the visitor does not support null values.
     */
    @SuppressWarnings("java:S1452")
    @NonNull <T extends B> EntityVisitor<? extends R, T, ? super B, ? super P, ? super C> visitorFor(T value);
}
