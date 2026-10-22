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
package com.phonepe.columbus.search.sort.visitor;

import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Sort} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Sort} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface SortTransformationVisitor<R, T extends Sort, P, C extends SortTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, Sort, P, C> {

    @Override
    R visit(T sort, P props, C context);
}
