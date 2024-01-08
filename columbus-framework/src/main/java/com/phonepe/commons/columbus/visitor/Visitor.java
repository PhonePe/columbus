package com.phonepe.commons.columbus.visitor;

import com.phonepe.commons.columbus.visitor.impl.EntityVisitor;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import com.phonepe.commons.columbus.visitor.impl.VisitorResolver;

/**
 * An object that can visit different types bounded by {@code B} to compute a result of type {@code R}.
 * <p>
 * This is very similar to the visitor design pattern, except the double-dispatch is replaced by a manual,
 * resolver-based, dispatch.
 *
 * <p>
 * A visitor generally has three components:
 * <ol>
 *   <li>
 *     The models which dispatch to the correct visitor method. Usually implemented by an {@code accept()} method.
 *   </li>
 *   <li>
 *     The methods which define the different logic for each type of model. A visitor has a fixed result that it
 *     computes across all methods.
 *   </li>
 *   <li>
 *     The closure / context in which visitor is created, which includes any additional data that is accessible from
 *     all the methods.
 *   </li>
 * </ol>
 * <p>
 * These components are implemented and managed separately in {@link VisitorResolver}, {@link EntityVisitor}, and
 * {@link VisitorContext}, respectively.
 * <p>
 * The context available to a visitor can be further split into 2 types - the data that remains same across all
 * invocations, and the data that could be different for each invocation. This interface supports providing this
 * changing data via {@code props} to avoid having to create a new instance of visitor for each invocation.
 *
 * <p>
 * With the {@link VisitorResolver} being created at runtime, it makes it possible to add more {@link EntityVisitor}s
 * to it, allowing extension across modules, without introducing circular dependency.
 *
 * @param <R> the return type of the visitor.
 * @param <B> the base type the visitor can visit.
 * @param <P> the type of additional context the visitor supports.
 * @see VisitorContext
 * @see VisitorResolver
 * @see EntityVisitor
 */
public interface Visitor<R, B, P> {

    <T extends B> R visit(T value, P props);

    <T extends B> R visit(T value);
}
