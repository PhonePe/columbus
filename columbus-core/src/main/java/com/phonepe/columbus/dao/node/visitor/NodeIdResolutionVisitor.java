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
package com.phonepe.columbus.dao.node.visitor;

import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link NodeId} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link NodeId} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface NodeIdResolutionVisitor<R, T extends NodeId, P, C extends NodeIdResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, NodeId, P, C> {

    @Override
    R visit(T node, P props, C context);
}
