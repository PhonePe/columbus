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
import com.phonepe.columbus.dao.node.NodeIdResolver;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link NodeIdResolver} that uses visitor to convert {@link NodeId} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class NodeIdResolutionContext<R, P, C extends NodeIdResolutionContext<R, P, C>>
        extends VisitorContext<R, NodeId, P, C> implements NodeIdResolver<R, P> {

    @Override
    public R resolve(final NodeId node, final P context) {
        return visit(node, context);
    }

    @Override
    public R resolve(final NodeId node) {
        return visit(node);
    }
}
