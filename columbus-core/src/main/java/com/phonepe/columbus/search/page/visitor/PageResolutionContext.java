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
package com.phonepe.columbus.search.page.visitor;

import com.phonepe.columbus.search.page.PageBounds;
import com.phonepe.columbus.search.page.PageResolver;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link PageResolver} that uses visitor to convert {@link PageBounds} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class PageResolutionContext<R, P, C extends PageResolutionContext<R, P, C>>
        extends VisitorContext<R, PageBounds, P, C> implements PageResolver<R, P> {

    @Override
    public R resolve(final PageBounds page, final P context) {
        return visit(page, context);
    }

    @Override
    public R resolve(final PageBounds page) {
        return visit(page);
    }
}
