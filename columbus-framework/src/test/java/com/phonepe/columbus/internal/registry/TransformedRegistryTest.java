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

class TransformedRegistryTest {

    private static final MutableRegistry<String, Integer> delegate = Registries.stored();
    private static final Registry<String, String> registry = Registries.transforming(
            Transforms::keyTransform,
            delegate,
            Transforms::valueTransform
    );

    @Test
    @DisplayName("successful transforms")
    void testSuccessfulTransform() {
        assertEquals("10", registry.get("Key1").orElse(null));
        assertEquals("10", registry.get("KEY1").orElse(null));
        assertEquals("20", registry.get("key2").orElse(null));
    }

    @Test
    @DisplayName("missing value")
    void testMissingValue() {
        assertTrue(registry.get("missing").isEmpty());
    }

    @Test
    @DisplayName("key rejected")
    void testKeyReject() {
        assertTrue(registry.get("a very long key").isEmpty());
        assertTrue(registry.get(null).isEmpty());
    }

    @Test
    @DisplayName("value rejected")
    void testValueReject() {
        assertTrue(registry.get("key3").isEmpty());
        assertTrue(registry.get("KeY3").isEmpty());
    }

    @BeforeAll
    static void beforeAll() {
        delegate.register("key1", 10);
        delegate.register("key2", 20);
        delegate.register("key3", -30);
    }
}
