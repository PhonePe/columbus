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
package com.phonepe.columbus.search.algorithms.common.distance.keyboard;

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("java:S1170")
public abstract class KeyboardDistance implements Distance<Character> {

    @Builder.Default
    private final double adjacent = 0.1f;
    @Builder.Default
    private final double diagonal = 0.4f;
    @Builder.Default
    private final double distant = 1f;

    @Override
    public double distance(@NonNull Character from, @NonNull Character to) {
        return getDistance(getKeyboard().getConnection(from, to));
    }

    protected abstract Keyboard getKeyboard();

    protected double getDistance(final Keyboard.Connection connection) {
        return switch (connection) {
            case SAME -> 0;
            case ADJACENT -> adjacent;
            case DIAGONAL -> diagonal;
            case DISTANT -> distant;
        };
    }
}
