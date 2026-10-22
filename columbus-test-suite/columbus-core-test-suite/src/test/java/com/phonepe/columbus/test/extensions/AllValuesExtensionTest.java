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
package com.phonepe.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.internal.serde.JsonSerDe;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.test.annotations.WithSerDe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

@WithSerDe.All
class AllValuesExtensionTest {

    @Test
    @DisplayName("provides mapper and serde")
    void testSerDeIsPresent(final SerDe serDe, final ObjectMapper mapper) {
        assertInstanceOf(JsonSerDe.class, serDe, "must provide a valid json serde");
        assertSame(mapper, ((JsonSerDe) serDe).getMapper(), "must use the mapper provided");
    }
}
