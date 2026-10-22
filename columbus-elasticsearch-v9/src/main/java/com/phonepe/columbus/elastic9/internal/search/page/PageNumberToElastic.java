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
import com.phonepe.columbus.search.page.PageNumber;

@RegistryKey(name = PageNumber.TYPE, type = PageNumber.class)
public class PageNumberToElastic<C extends ElasticPageResolutionContext<C>> implements PageToElastic<PageNumber, C> {

    @Override
    public ElasticPage visit(final PageNumber page, final Object props, final C context) {
        final var size = validatePageSize(page.getSize());
        return ElasticPage.builder()
                .size(size)
                .from(getPageOffset(page.getNumber(), size))
                .build();
    }

    private static Integer validatePageSize(final Integer size) {
        if (size != null && size < 0) {
            throw new IllegalArgumentException("page size must be a positive integer, got %s".formatted(size));
        }
        return size;
    }

    private static Integer getPageOffset(final Integer number, final Integer size) {
        if (number == null) {
            return null;
        }
        if (number < 0) {
            throw new IllegalArgumentException("page number must be a non negative integer, got %s".formatted(number));
        }
        if (number == 0) {
            return 0;
        }
        if (size == null) {
            throw new IllegalArgumentException("page size must be provided when page number is more than zero");
        }
        return number * size;
    }
}
