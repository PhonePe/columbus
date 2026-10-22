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
package com.phonepe.columbus.search.page;

import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of {@link PageBounds} instances.
 */
@UtilityClass
public class Pages {

    private static final PageBounds EMPTY = offset(0, 0);

    public PageBounds empty() {
        return EMPTY;
    }

    public PageNumber number(final int size, final int number) {
        return PageNumber.builder()
                .size(size)
                .number(number)
                .build();
    }

    public PageOffset offset(final int size, final int offset) {
        return PageOffset.builder()
                .size(size)
                .offset(offset)
                .build();
    }

    public PagePointer pointer(final int size, final Object pointer) {
        return PagePointer.builder()
                .size(size)
                .pointer(pointer)
                .build();
    }
}
