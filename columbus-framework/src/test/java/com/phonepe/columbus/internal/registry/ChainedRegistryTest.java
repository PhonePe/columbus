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
package com.phonepe.columbus.internal.registry;

import com.phonepe.columbus.registry.MutableRegistry;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChainedRegistryTest {

    private static final MutableRegistry<String, Integer> registry1 = Registries.stored();
    private static final MutableRegistry<String, Integer> registry2 = Registries.stored();
    private static final Registry<String, Integer> registry = Registries.chaining(registry1)
            .add(registry2)
            .build();

    @Test
    @DisplayName("fetch from first")
    void testFetchFromFirst() {
        assertEquals(10, registry.get("key1").orElse(null));
    }

    @Test
    @DisplayName("fetch from second")
    void testFetchFromSecond() {
        assertEquals(20, registry.get("key2").orElse(null));
    }

    @Test
    @DisplayName("missing value")
    void testMissingValue() {
        assertTrue(registry.get("missing").isEmpty());
    }

    @BeforeAll
    static void beforeAll() {
        registry1.register("key1", 10);
        registry2.register("key1", 30);
        registry2.register("key2", 20);
    }
}
