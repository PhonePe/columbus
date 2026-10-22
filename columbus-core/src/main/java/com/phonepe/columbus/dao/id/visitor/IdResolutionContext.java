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
package com.phonepe.columbus.dao.id.visitor;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.id.IdResolver;
import com.phonepe.columbus.visitor.impl.VisitorContext;
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
