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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class MonitoredRegistryTest {

    private static final MonitoredRegistry.Listener<String, Integer> listener = mock();
    private static final MutableRegistry<String, Integer> delegate = Registries.stored();

    private static final Registry<String, Integer> registry = Registries.monitorIfNotEmpty(delegate, listener);

    @Test
    @DisplayName("listener success")
    void testListenerSuccess() {
        assertEquals(10, registry.get("key1").orElse(null));
        verify(listener, times(1)).success("key1", 10);
        verifyNoMoreInteractions(listener);
    }

    @Test
    @DisplayName("listener failure")
    void testListenerFailure() {
        assertTrue(registry.get("missing").isEmpty());
        verify(listener, times(1)).failure("missing");
        verifyNoMoreInteractions(listener);
    }

    @AfterEach
    @SuppressWarnings("unchecked")
    void tearDown() {
        reset(listener);
    }

    @BeforeAll
    static void beforeAll() {
        delegate.register("key1", 10);
    }
}
