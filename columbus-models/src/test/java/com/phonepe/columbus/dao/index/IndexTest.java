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
package com.phonepe.columbus.dao.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexTest {

    @Test
    @DisplayName("index name")
    void testIndexName() {
        final var index = Indices.name("name");
        assertAll(
                "index name",
                () -> assertEquals(IndexName.TYPE, index.getType(), "must match type"),
                () -> assertEquals("name", index.getName(), "must contain provided name"));
    }

    @Test
    @DisplayName("index reference")
    void testIndexReference() {
        final var index = Indices.reference("key");
        assertAll(
                "index reference",
                () -> assertEquals(IndexReference.TYPE, index.getType(), "must match type"),
                () -> assertEquals("key", index.getKey(), "must contain provided key"));
    }
}
