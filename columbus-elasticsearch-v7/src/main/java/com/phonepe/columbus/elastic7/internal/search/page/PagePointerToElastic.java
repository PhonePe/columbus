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
package com.phonepe.columbus.elastic7.internal.search.page;

import com.phonepe.columbus.elastic7.search.page.ElasticPage;
import com.phonepe.columbus.elastic7.search.page.ElasticPageResolutionContext;
import com.phonepe.columbus.elastic7.search.page.PageToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.page.PagePointer;

import java.util.List;

@RegistryKey(name = PagePointer.TYPE, type = PagePointer.class)
public class PagePointerToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PagePointer, C> {

    @Override
    public ElasticPage visit(final PagePointer page, final Object props, final C context) {
        return ElasticPage.builder()
                .size(page.getSize())
                .afterKeys(getKeys(page.getPointer()))
                .build();
    }

    private List<?> getKeys(final Object pointer) {
        if (pointer == null) {
            return List.of();
        }
        if (pointer instanceof List<?> keys) {
            return keys;
        }
        return List.of(pointer);
    }
}
