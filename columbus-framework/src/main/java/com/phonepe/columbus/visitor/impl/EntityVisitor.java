/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.visitor.impl;

/**
 * A visitor is responsible for performing an operation on a specific type of data, with the given context and props.
 * <p>
 * Each {@link EntityVisitor} instance represents a visit method in the well-known Visitor pattern.
 *
 * @param <R> the return type of the visitor.
 * @param <T> the type of the value this visitor visits.
 * @param <B> the base type the visitors in this group can visit.
 * @param <P> the type of props the visitor supports.
 * @param <C> the type of context this visitor needs, to function.
 */
public interface EntityVisitor<R, T extends B, B, P, C extends VisitorContext<R, B, P, C>> {

    R visit(T value, P props, C context);
}
