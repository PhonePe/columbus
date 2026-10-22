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
package com.phonepe.columbus.search.misc.result;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents the count of elements. If the {@link #count} is an approximate lower bound, {@link #exact} will be false.
 */
@Data
@Builder
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public class TotalCount {

    private final long count;

    @Builder.Default
    @Accessors(fluent = true)
    private final boolean exact = true;

    public static TotalCount exact(final long count) {
        return TotalCount.builder()
                .count(count)
                .exact(true)
                .build();
    }

    public static TotalCount atLeast(final long count) {
        return TotalCount.builder()
                .count(count)
                .exact(false)
                .build();
    }
}
