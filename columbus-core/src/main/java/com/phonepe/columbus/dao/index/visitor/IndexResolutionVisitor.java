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
package com.phonepe.columbus.dao.index.visitor;

import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link Index} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Index} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface IndexResolutionVisitor<R, T extends Index, P, C extends IndexResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, Index, P, C> {

    @Override
    R visit(T index, P props, C context);
}
