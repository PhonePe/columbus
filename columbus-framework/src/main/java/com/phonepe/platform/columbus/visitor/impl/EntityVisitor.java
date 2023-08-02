package com.phonepe.platform.columbus.visitor.impl;

/**
 * A visitor is responsible for performing an operation on a specific type of data, with the given context.
 * <p>
 * Each {@link EntityVisitor} instance represents a visit method in the well-known Visitor pattern.
 *
 * @param <R> the return type of the visitor.
 * @param <T> the type of the value this visitor visits.
 * @param <B> the base type the visitors in this group can visit.
 * @param <C> the type of context this visitor needs, to function.
 */
public interface EntityVisitor<R, T extends B, B, C extends VisitorContext<R, B, C>> {

    R visit(T value, C context);
}
