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
package com.phonepe.columbus.elastic9.internal.search.page;

import com.phonepe.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.columbus.elastic9.search.page.ElasticPageResolutionContext;
import com.phonepe.columbus.elastic9.search.page.PageToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.page.PageOffset;

@RegistryKey(name = PageOffset.TYPE, type = PageOffset.class)
public class PageOffsetToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PageOffset, C> {

    @Override
    public ElasticPage visit(final PageOffset page, final Object props, final C context) {
        return ElasticPage.builder()
                .size(page.getSize())
                .from(page.getOffset())
                .build();
    }
}
